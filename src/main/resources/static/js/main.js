'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

// Note: 'stompClient' is used to manage the WebSocket connection established via SockJS with STOMP protocol.
var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
// Note: The 'connect' function handles the establishment of the WebSocket connection.
// It uses SockJS and STOMP for real-time messaging and sends a JWT token for authentication its hard coded here just for testing.
// The '/ws' endpoint corresponds to the endpoint defined in the WebSocketConfig.
function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        var token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9QTEFZRVIiLCJzdWIiOiIyIiwiaWF0IjoxNzQwNDc3MzYyLCJleHAiOjk5OTk5OTk5OTk5fQ.kx6SvMt0zzK4X3K0AtDxa7fikQkVsprEWE1cNDIRNv8";
        stompClient.connect(
            { Authorization: "Bearer " + token },
            onConnected,
            onError
        );
    }
    event.preventDefault();
}

//This REST API call is separate from the WebSocket connection and is used to
//populate the chat window with previous messages. It fetches data from the
// /chat/messages/{chatId} endpoint defined in the back-end controllers. The
//function uses the fetch API to make a GET request and processes the JSON
//response to display the messages in the chat window.
function fetchAndDisplayOldMessages(chatId) {
    fetch(`/chat/messages/${chatId}`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9QTEFZRVIiLCJzdWIiOiIyIiwiaWF0IjoxNzQwNDc3MzYyLCJleHAiOjk5OTk5OTk5OTk5fQ.kx6SvMt0zzK4X3K0AtDxa7fikQkVsprEWE1cNDIRNv8"
        }
    })
        .then(response => response.json())
        .then(messages => {
            // Check if there are any messages
            if (messages && messages.length > 0) {
                messages.forEach(message => {
                    displayMessage(message);
                });
            } else {
                console.log('No previous messages found');
            }
        })
        .catch(error => {
            console.error('Error fetching messages:', error);
        });
}
// Note: 'displayMessage' dynamically creates HTML elements to render chat messages.
// It handles different message types ('JOIN', 'LEAVE', or 'CHAT') and appends them to the chat history. noted i removed all types we only have CHAT
// Function to display a message
function displayMessage(message) {
    var messageElement = document.createElement('li');

    // If the message is a 'join' or 'leave' type, handle differently
    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        messageElement.textContent = (message.sender && message.sender.username) + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        messageElement.textContent = (message.sender && message.sender.username) + ' left!';
    } else {
        // Normal chat message
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var senderName = message.sender && message.sender.username ? message.sender.username : "Unknown";
        var avatarText = document.createTextNode(senderName.charAt(0).toUpperCase());
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(senderName);
        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(senderName);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    // Message content
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.message || "No content");
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);

    // Optional: Display timestamp (format it to your preference)
    var timestampElement = document.createElement('span');
    var timestampText = document.createTextNode(new Date(message.timeStamp).toLocaleString());
    timestampElement.appendChild(timestampText);
    messageElement.appendChild(timestampElement);

    // Add message element to the message area
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight; // Scroll to the bottom
}

// Function to get a random color for the avatar based on the username
//just for the color forget about it
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}
// Note: After a successful connection, we subscribe to channels for both live updates and historical data.
// The '/topic/chat/{sessionID}' channel broadcasts live messages, while '/user/queue/history' returns previous messages.
// Note: The client subscribes to '/user/queue/history' to listen for messages that are targeted
// specifically to the connected user. These messages are delivered when the user has joined a chat
// and the backend sends historical messages via convertAndSendToUser().
// This destination is not explicitly defined on the server; it is created automatically by Spring's user
// destination mechanism. The server effectively sends messages to '/user/{username}/queue/history'.
// The client sees this as '/user/queue/history'.

function onConnected() {
    if (stompClient && stompClient.connected) {
        stompClient.unsubscribe('/topic/chat/6');
    }
    //1 here is the session ID
// the one here should be dynamic based on teh chat the user wants to join the end point is /topic/chat/{sessionID}
    stompClient.subscribe('/topic/chat/6', onMessageReceived);

    // Subscribe to receive previous messages only for the new user
    stompClient.subscribe('/user/queue/history', onPreviousMessagesReceived);

    // Send request to fetch previous messages via WebSocket given chat ID
    requestPreviousMessages(1);

    connectingElement.classList.add('hidden');
}
// Note: The request sends a message to the '/app/join/{chatId}' endpoint. The back-end retrieves previous messages and pushes them to the user's private '/queue/history' destination.
// the method is in chat controller that returns previouse messages the full api endpoint is /app/join/{chatid}
function requestPreviousMessages(chatId) {
    stompClient.send("/app/join/" + chatId, {}, {});
}
// Note: This callback handles messages sent back to the '/user/queue/history' subscription, enabling the display of chat history to the newly connected user.
function onPreviousMessagesReceived(payload) {
    let messages = JSON.parse(payload.body);
    messages.forEach(message => {
        displayMessage(message);
    });
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}
// Note: The 'sendMessage' function sends chat messages to the '/app/chat/1' endpoint, where the back-end processes the message (saves it, stamps it with a timestamp, and broadcasts it to all subscribers).

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            message: messageContent,
            type: 'CHAT'
        };
        //1 here is session ID
        stompClient.send("/app/chat/6", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    displayMessage(message);
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
//chat/messages/{chatID}
// Fetch and display old messages when the page loads (chatId = 1 for example)
fetchAndDisplayOldMessages(1); // Replace 1 with the actual chatId you want to fetch
