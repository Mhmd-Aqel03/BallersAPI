package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.SessionCreationException;
import com.ballersApi.ballersApi.exceptions.SessionNotFoundException;
import com.ballersApi.ballersApi.models.Chat;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionTeamRepository sessionTeamRepository;

        public List<Session> getAllUpcomingSessions () {
            try{
        return sessionRepository.findByMatchDateTimeAfter(LocalDateTime.now());
    } catch (Exception e){
                throw new RuntimeException("Error fetching upcoming sessions", e);
            }
    }
    //For the Admin

    public Session createSession(Session session) {
        try {
            if (session == null) {
                throw new SessionCreationException("Session data is missing");
            }
            Chat chat = new Chat();
            chat.setSession(session);
            session.setChat(chat);
            return sessionRepository.save(session);
        } catch (Exception e) {
            throw new SessionCreationException("Error creating session: " + e.getMessage());
        }

    }

    public void deleteSession(Long sessionId) {

        try {
           Session session1 = sessionRepository.findById(sessionId).orElse(null);
            if (session1==null) {
                throw new SessionNotFoundException("Session with ID " + sessionId + " not found");
            }
            sessionRepository.deleteById(sessionId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting session: " + e.getMessage(), e);
        }
    }
    }





