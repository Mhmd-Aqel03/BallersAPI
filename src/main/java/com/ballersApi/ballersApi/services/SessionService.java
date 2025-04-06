package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.exceptions.SessionCreationException;
import com.ballersApi.ballersApi.exceptions.SessionNotFoundException;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionTeamRepository sessionTeamRepository;

    public List<Session> getAllUpcomingSessions() {
        try {
            return sessionRepository.findByMatchDateTimeAfter(LocalDateTime.now());
        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Error fetching upcoming sessions: " + e.getMessage());
        }
    }
    //For the Admin

    public void createSession(Session session) {
        try {
            if (session == null) {
                throw new SessionCreationException("Session data is missing");
            }

            sessionRepository.save(session);
        } catch (Exception e) {
            throw new SessionCreationException("Error creating session: " + e.getMessage());
        }

    }

    public void deleteSession(Long sessionId) {

        try {
            Session session1 = sessionRepository.findById(sessionId).orElse(null);
            if (session1 == null) {
                throw new SessionNotFoundException("Session with ID " + sessionId + " not found");
            }
            sessionRepository.deleteById(sessionId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting session: " + e.getMessage(), e);
        }
    }

    public void updateSession(Long id, Session newSession) {
        Session session = sessionRepository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session with ID " + id + " not found"));

        session.setMatchDateTime(newSession.getMatchDateTime());
        session.setMaxPlayers(newSession.getMaxPlayers());
        session.setPrice(newSession.getPrice());
        session.setCourt(newSession.getCourt());
        session.setReferee(newSession.getReferee());

        try{
            sessionRepository.save(session);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Error saving session: " + e.getMessage());
        }
    }
}





