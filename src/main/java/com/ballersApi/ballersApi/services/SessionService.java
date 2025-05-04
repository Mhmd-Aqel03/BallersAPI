package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.exceptions.CanNotFetchDataException;
import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.exceptions.SessionCreationException;
import com.ballersApi.ballersApi.exceptions.SessionNotFoundException;
import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
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
    @Autowired
    RefereeService refereeService;
    @Autowired
    CourtService courtService;

        public List<Session> getAllUpcomingSessions () {
            try{
        return sessionRepository.findByMatchDateAfter(LocalDate.now());
    } catch (Exception e){
                throw new CanNotFetchDataException("Error fetching upcoming sessions");
            }
    }
    public Optional<Session> getSessionById(Long sessionId) {

        Session session1 = sessionRepository.findById(sessionId).orElse(null);
        if (session1==null) {
            throw new SessionNotFoundException("Session with ID " + sessionId + " not found");
        }
        System.out.println("A "+session1.getTeamA().getId());
        System.out.println("B "+session1.getTeamB().getId());
        return Optional.ofNullable(session1);
    }
    public Map<String, List<Session>> getSessionsForWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        List<Session> sessions = sessionRepository.findByMatchDateBetween(startDate, endDate);

        // Group by date
        Map<LocalDate, List<Session>> groupedByDate = sessions.stream()
                .collect(Collectors.groupingBy(Session::getMatchDate));

        // Sort the outer map by LocalDate (natural order), and the inner lists by matchStartTime
        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // ensures Saturday comes before Sunday, etc.
                .collect(Collectors.toMap(
                        entry -> {
                            LocalDate date = entry.getKey();
                            String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                            return dayOfWeek + " " + date;
                        },
                        entry -> entry.getValue().stream()
                                .sorted(Comparator.comparing(Session::getMatchStartTime))
                                .collect(Collectors.toList()),
                        (v1, v2) -> v1,
                        LinkedHashMap::new
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
    public void updateSession(Long id, SessionDTO newSession) {
        Session session = sessionRepository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session with ID " + id + " not found"));

        session.setMatchDate(newSession.getMatchDate());
        session.setMatchStartTime(newSession.getMatchStartTime());
        session.setMatchEndTime(newSession.getMatchEndTime());
        session.setMaxPlayers(newSession.getMaxPlayers());
        session.setPrice(newSession.getPrice());
        session.setType(newSession.getType());

        if (newSession.getCourtId() != -1) {
            Court court = courtService.getCourtById(newSession.getCourtId());
            session.setCourt(court);
        }

        if (newSession.getRefereeId() != -1) {
            User referee = refereeService.getRefereeById(newSession.getRefereeId());
            session.setReferee(referee);
        }

        try {
            sessionRepository.save(session);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Error saving session: " + e.getMessage());
        }
    }
    }





