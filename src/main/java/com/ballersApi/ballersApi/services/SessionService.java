package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.AdminSessionDTO;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    SessionTeamService sessionTeamService;

    public List<Session> getAllUpcomingSessions() {
        try {
            return sessionRepository.findByMatchDateAfterAndIsDoneFalse(LocalDate.now());
        } catch (Exception e) {
            throw new CanNotFetchDataException("Error fetching upcoming sessions");
        }
    }

    public Optional<Session> getSessionById(Long sessionId) {

        Session session1 = sessionRepository.findById(sessionId).orElse(null);
        if (session1 == null) {
            throw new SessionNotFoundException("Session with ID " + sessionId + " not found");
        }
        System.out.println("A " + session1.getTeamA().getId());
        System.out.println("B " + session1.getTeamB().getId());
        return Optional.ofNullable(session1);
    }

    public Map<String, List<Session>> getSessionsForWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        List<Session> sessions = sessionRepository.findByMatchDateBetweenAndIsDoneFalse(startDate, endDate);

        if (sessions.isEmpty()) {
            throw new SessionNotFoundException("there are no sessions available for this week");
        }


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
        sessionTeamService.deleteAllTeamSessions(sessionId);

        try {
            Session session1 = sessionRepository.findById(sessionId).orElse(null);
            if (session1 == null) {
                throw new SessionNotFoundException("Session with ID " + sessionId + " not found");
            }
            sessionRepository.deleteById(sessionId);
        } catch (Exception e) {
            throw new SessionCreationException("Error deleting session: " + e.getMessage());
        }
    }


    public void updateSession(Long id, AdminSessionDTO adminSessionDTO) {
        Session session = sessionRepository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session with ID " + id + " not found"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        session.setType(adminSessionDTO.getType());
        session.setMatchDate(adminSessionDTO.getMatchDate());
        session.setMatchStartTime(LocalTime.parse(adminSessionDTO.getMatchStartTime(), formatter));
        session.setMatchEndTime(LocalTime.parse(adminSessionDTO.getMatchEndTime(), formatter));
        session.setMaxPlayers(adminSessionDTO.getMaxPlayers());
        session.setPrice(adminSessionDTO.getPrice());

        if (!adminSessionDTO.getCourtId().equals("-1")) {
            Court court = courtService.getCourtById(Long.parseLong(adminSessionDTO.getCourtId()));
            session.setCourt(court);
        }

        if (!adminSessionDTO.getRefereeId().equals("-1")) {
            User referee = refereeService.getRefereeById(Long.parseLong(adminSessionDTO.getRefereeId()));
            session.setReferee(referee);
        }

        try {
            sessionRepository.save(session);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Error saving session: " + e.getMessage());
        }
    }

    public List<AdminSessionDTO> getAllSessions() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
        List<Session> sessions;
        List<AdminSessionDTO> adminSessions = new ArrayList<>();

        try {
            sessions = sessionRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Error fetching upcoming sessions: " + e.getMessage());
        }

        for (Session session : sessions) {
            AdminSessionDTO adminSessionDTO = new AdminSessionDTO();

            adminSessionDTO.setId(session.getId());
            adminSessionDTO.setType(session.getType());
            adminSessionDTO.setMatchDate(session.getMatchDate());
            adminSessionDTO.setMatchStartTime(session.getMatchStartTime().format(dtf));
            adminSessionDTO.setMatchEndTime(session.getMatchEndTime().format(dtf));
            adminSessionDTO.setMaxPlayers(session.getMaxPlayers());
            adminSessionDTO.setPrice(session.getPrice());

            adminSessionDTO.setCourtId(session.getCourt() != null ? session.getCourt().getId().toString() : "None");

            adminSessionDTO.setRefereeId(session.getReferee() != null ? session.getReferee().getId().toString() : "None");

            adminSessions.add(adminSessionDTO);
        }

        return adminSessions;
    }
}





