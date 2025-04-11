package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.models.ChatMessage;
import com.ballersApi.ballersApi.repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessagesByChatId(Long chatId) {
        return chatMessageRepository.findByChatId(chatId);
    }

    public List<ChatMessage> getLast10Messages(Long chatId) {
        return chatMessageRepository.findTop10ByChatIdOrderByTimeStampDesc(chatId);
    }
    public List<ChatMessage> getPreviousMessages(Long chatId) {
        return chatMessageRepository.findByChatIdOrderByTimeStampAsc(chatId);
    }
}
