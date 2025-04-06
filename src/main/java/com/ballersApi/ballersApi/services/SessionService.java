package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.CanNotFetchDataException;
import com.ballersApi.ballersApi.exceptions.SessionCreationException;
import com.ballersApi.ballersApi.exceptions.SessionNotFoundException;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionTeamRepository sessionTeamRepository;
    @Autowired
    private PlayerRepository playerRepository;

        public List<Session> getAllUpcomingSessions () {
            try{
        return sessionRepository.findByMatchDateAfter(LocalDate.now());
    } catch (Exception e){
                throw new CanNotFetchDataException("Error fetching upcoming sessions");
            }
    }
    public Optional<Session> getSessionById(Long sessionId) {
            return sessionRepository.findById(sessionId);

    }
    public Map<String, List<Session>> getSessionsForWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        List<Session> sessions = sessionRepository.findByMatchDateBetween(startDate, endDate);

        // Temporary map grouped by date
        Map<LocalDate, List<Session>> groupedByDate = sessions.stream()
                .collect(Collectors.groupingBy(Session::getMatchDate));

        // Sort by day of week (Sunday to Saturday)
        return groupedByDate.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getDayOfWeek()))
                .collect(Collectors.toMap(
                        entry -> {
                            LocalDate date = entry.getKey();
                            String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                            return dayOfWeek + " " + date; // e.g., "Monday 2025-04-01"
                        },
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new // Keeps order
                ));
    }

    //For the Admin

    public Session createSession(Session session) {
        try {
            if (session == null) {
                throw new SessionCreationException("Session data is missing");
            }

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
            throw new SessionCreationException("Error deleting session: " + e.getMessage());
        }
    }

    }





