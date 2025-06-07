package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.RefereeDTO;
import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.exceptions.*;
import com.ballersApi.ballersApi.models.*;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RefereeService {
    @Autowired
    private UserService userService;
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public User getRefereeById(long id) {
        User user = userService.getUserById(id);

        if(user.getRole() != Role.ROLE_REFEREE){
            throw new UserNotFoundException("Referee with id: " + id + " not found");
        }

        return user;
    }

    public void addReferee(RefereeDTO refereeDTO){
        User referee = new User();

        userService.checkUserInput(refereeDTO.getUsername(), refereeDTO.getPassword(), refereeDTO.getEmail());

        referee.setUsername(refereeDTO.getUsername());
        referee.setPassword(refereeDTO.getPassword());
        referee.setEmail(refereeDTO.getEmail());
        referee.setRole(Role.ROLE_REFEREE);

        userService.addUser(referee);
    }

    public ArrayList<User> getAllReferees(){

        return userService.getUsersByRole(Role.ROLE_REFEREE);

    }

    public void deleteReferee(long id){
        userService.deleteUser(id);
    }
    @Transactional
    public void finalizeSessionResult(Long sessionId, Team team) {
        // Fetch the session by ID
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        // Check if the session has already been finalized
        if (session.getWinningTeam() != null) {
            throw new SessionFinalizedException("This session has already been finalized.");
        }

        List<Player> teamAPlayers = session.getTeamA().getPlayers();
        List<Player> teamBPlayers = session.getTeamB().getPlayers();

        // Set winning team in the session
        if (team.equals(Team.A)) {
            session.setWinningTeam(team);
            session.setDone(true);
            sessionRepository.save(session);

            for (Player player : teamAPlayers) {
                player.setSessionsPlayed(player.getSessionsPlayed() + 1);
                player.setSessionsWon(player.getSessionsWon() + 1);

                // Store only the session ID and the result (true for win)
                player.getPastSessions().put(session.getId(), true);

                playerRepository.save(player);
            }

            for (Player player : teamBPlayers) {
                player.setSessionsPlayed(player.getSessionsPlayed() + 1);
                player.setSessionsLost(player.getSessionsLost() + 1);

                // Store only the session ID and the result (false for loss)
                player.getPastSessions().put(session.getId(), false);

                playerRepository.save(player);
            }

        } else {
            session.setWinningTeam(team);
            session.setDone(true);
            sessionRepository.save(session);

            for (Player player : teamAPlayers) {
                player.setSessionsPlayed(player.getSessionsPlayed() + 1);
                player.setSessionsLost(player.getSessionsLost() + 1);

                // Store only the session ID and the result (false for loss)
                player.getPastSessions().put(session.getId(), false);

                playerRepository.save(player);
            }

            for (Player player : teamBPlayers) {
                player.setSessionsPlayed(player.getSessionsPlayed() + 1);
                player.setSessionsWon(player.getSessionsWon() + 1);

                // Store only the session ID and the result (true for win)
                player.getPastSessions().put(session.getId(), true);

                playerRepository.save(player);
            }
        }
    }

    public Map<String, List<SessionDTO>> getSessionsByRefereeId(long refereeId) {
        User referee = userService.getUserById(refereeId);

        if (referee.getRole() != Role.ROLE_REFEREE) {
            throw new UserNotFoundException("User with ID " + refereeId + " is not a referee.");
        }

        List<Session> sessions = sessionRepository.findByRefereeAndIsDoneFalse(referee);

        if (sessions.isEmpty()) {
            throw new SessionNotFoundException("No sessions found for referee with ID " + refereeId);
        }

        // Sort sessions by date then time
        sessions.sort(Comparator
                .comparing(Session::getMatchDate)
                .thenComparing(Session::getMatchStartTime));

        // Map<LocalDate, List<SessionDTO>>
        Map<LocalDate, List<SessionDTO>> groupedByDate = sessions.stream()
                .map(session -> new SessionDTO(session, userService))
                .collect(Collectors.groupingBy(
                        SessionDTO::getMatchDate,
                        TreeMap::new,  // Automatically sorts by LocalDate
                        Collectors.toList()
                ));

        // Convert to Map<String, List<SessionDTO>> with "Sunday 2025-06-01" as key
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE yyyy-MM-dd", Locale.ENGLISH);

        Map<String, List<SessionDTO>> formattedMap = new LinkedHashMap<>();
        groupedByDate.forEach((date, dtoList) -> {
            String key = date.format(formatter);
            formattedMap.put(key, dtoList);
        });

        return formattedMap;
    }
    public void chooseMvp(Long sessionId, Long playerId) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found with ID: " + sessionId));

        // Check if session has been finalized
        if (session.getWinningTeam() == null) {
            throw new SessionFinalizedException("Cannot choose MVP. The session has not been finalized yet. Please finalize the session first.");
        }
        // Check if MVP has already been chosen
        if (session.getMvp() != null) {
            throw new MvpSelectionException("MVP has already been chosen for this session.");
        }
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + playerId));

        // Ensure the player participated in the session
        boolean isInTeamA = session.getTeamA() != null && session.getTeamA().getPlayers().contains(player);
        boolean isInTeamB = session.getTeamB() != null && session.getTeamB().getPlayers().contains(player);

        if (!isInTeamA && !isInTeamB) {
            throw new PlayerNotFoundException("Player did not participate in this session");
        }

        // Set MVP
        session.setMvp(player);
        sessionRepository.save(session);

        // Increment MVP count
        player.setMVPs(player.getMVPs() + 1);
        playerRepository.save(player);
    }


}
