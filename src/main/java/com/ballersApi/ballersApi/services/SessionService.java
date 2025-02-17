package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> getAllUpcomingSessions() {
        return sessionRepository.findBymatchDateTimeAfter(LocalDateTime.now());
    }
    //For the Admin
    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }
    public void deleteSession(Session session) {
         sessionRepository.delete(session);
    }




}
