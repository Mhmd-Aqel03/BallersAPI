package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.ChatMessage;
import com.ballersApi.ballersApi.services.ChatMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping("/{chatId}")
    public List<ChatMessage> getMessagesByChatId(@PathVariable Long chatId) {
        return chatMessageService.getMessagesByChatId(chatId);
    }
}
