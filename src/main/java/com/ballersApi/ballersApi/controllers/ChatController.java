package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.exceptions.SessionNotFoundException;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.models.Chat;
import com.ballersApi.ballersApi.models.ChatMessage;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.services.ChatMessageService;
import com.ballersApi.ballersApi.services.ChatService;
import com.ballersApi.ballersApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final ChatMessageService chatMessageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(ChatService chatService, UserService userService, ChatMessageService chatMessageService) {
        this.chatService = chatService;
        this.userService = userService;
        this.chatMessageService = chatMessageService;
        this.messagingTemplate = messagingTemplate;
    }

    // When a client sends a message to /app/chat/{sessionId},
    // it will be broadcast to /topic/chat/{sessionId}
    @MessageMapping("/chat/{sessionId}")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage sendMessage(@DestinationVariable Long sessionId, @Payload ChatMessage chatMessage, Principal principal) {
        // Debugging log
        System.out.println("Received message content: " + chatMessage.getMessage());
        String username = principal.getName();
        User sender = userService.getUserByUsername(username);
        if (sender == null) {
            throw new UserNotFoundException("User with username '" + username + "' not found.");
        }
        chatMessage.setSender(sender);

        Chat chat = chatService.getChatBySessionId(sessionId);

        chatMessage.setChat(chat);
        chatMessage.setTimeStamp(LocalDateTime.now());
        //save to database the messages
        chatMessageService.saveMessage(chatMessage);


        return chatMessage;
    }

    //using the simpmessagetemplate
    //Note: sendPreviousMessages() is mapped to /app/join/{chatId}.
    // It retrieves previous messages for the specified chat session (chatId) and sends them
    // back to the requesting user using messagingTemplate.convertAndSendToUser().
    // It retrieves historical messages for the chat and sends them to the user-specific destination (/user/{username}/queue/history)
    // Spring’s built-in user destination mechanism automatically prepends "/user" to the provided path.
    @MessageMapping("/join/{chatId}")
    public void sendPreviousMessages(@DestinationVariable Long chatId, Principal principal) {
        String username = principal.getName(); // Identify the new user

        // Retrieve previous chat messages from the database/service
        List<ChatMessage> previousMessages = chatMessageService.getPreviousMessages(chatId);

        // Send the previous messages only to the user who just joined.
        // Spring converts this to the effective destination: /user/{username}/queue/history.
        // Send only to the new user
        messagingTemplate.convertAndSendToUser(username, "/queue/history", previousMessages);
    }

    @GetMapping("/chat/session/{sessionId}")
    public ResponseEntity<Chat> getChatBySession(@PathVariable Long sessionId) {
        Chat chat = chatService.getChatBySessionId(sessionId);

        return ResponseEntity.ok(chat);
    }

    @GetMapping("/chat/messages/{chatId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long chatId) {
        List<ChatMessage> messages = chatMessageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }
}
