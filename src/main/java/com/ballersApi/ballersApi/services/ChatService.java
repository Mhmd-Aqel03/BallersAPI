package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.SessionNotFoundException;
import com.ballersApi.ballersApi.models.Chat;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public Chat createChat(Session session, List<Player> players) {
        Chat chat = new Chat();
        chat.setSession(session);
        chat.setPlayers(players);  // Ensure you have a ManyToMany mapping in `Chat`
        return chatRepository.save(chat);
    }

    public Chat getChatBySessionId(Long sessionId) {
        return chatRepository.findBySessionId(sessionId).orElseThrow(() -> new SessionNotFoundException("Chat for session ID '" + sessionId + "' not found."));
    }
}
