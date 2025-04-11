'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        var token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9QTEFZRVIiLCJzdWIiOiJtb2hhbmFkYWRnb2F0ZWQiLCJpYXQiOjE3NDA0NzczNjIsImV4cCI6OTk5OTk5OTk5OTl9.ywf1i6SKC6xF9mGz23LpZDjXgOpO7jYJ-UNHWXjVuFY";
        stompClient.connect(
            { Authorization: "Bearer " + token },
            onConnected,
            onError
        );
    }
    event.preventDefault();
}

// Fetch and display old messages for a specific chat
function fetchAndDisplayOldMessages(chatId) {
    fetch(`/chat/messages/${chatId}`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9QTEFZRVIiLCJzdWIiOiJtb2hhbmFkYWRnb2F0ZWQiLCJpYXQiOjE3NDA0NzczNjIsImV4cCI6OTk5OTk5OTk5OTl9.ywf1i6SKC6xF9mGz23LpZDjXgOpO7jYJ-UNHWXjVuFY"
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

function onConnected() {
    if (stompClient && stompClient.connected) {
        stompClient.unsubscribe('/topic/chat/6');
    }

    stompClient.subscribe('/topic/chat/6', onMessageReceived);

    // Subscribe to receive previous messages only for the new user
    stompClient.subscribe('/user/queue/history', onPreviousMessagesReceived);

    // Send request to fetch previous messages via WebSocket
    requestPreviousMessages(1);

    connectingElement.classList.add('hidden');
}

function requestPreviousMessages(chatId) {
    stompClient.send("/app/join/" + chatId, {}, {});
}

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

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            message: messageContent,
            type: 'CHAT'
        };
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

// Fetch and display old messages when the page loads (chatId = 1 for example)
fetchAndDisplayOldMessages(1); // Replace 1 with the actual chatId you want to fetch
