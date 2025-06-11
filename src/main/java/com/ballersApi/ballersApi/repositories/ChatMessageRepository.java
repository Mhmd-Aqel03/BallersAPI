package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatId(Long chatId); // Fetch messages by chat ID
    List<ChatMessage> findTop10ByChatIdOrderByTimeStampDesc(Long chatId);
    List<ChatMessage> findByChatIdOrderByTimeStampAsc(Long chatId);

}
