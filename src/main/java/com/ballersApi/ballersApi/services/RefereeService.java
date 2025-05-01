package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.RefereeDTO;
import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.exceptions.PlayerNotFoundException;
import com.ballersApi.ballersApi.exceptions.SessionFinalizedException;
import com.ballersApi.ballersApi.exceptions.SessionNotFoundException;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.models.*;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public void finalizeSessionResult(Long sessionId, Team team) {
        // Fetch the session by ID
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        if (session.getWinningTeam() != null) {
            throw new SessionFinalizedException("This session has already been finalized.");
        }


        List<Player> teamAPlayers = session.getTeamA().getPlayers();
        List<Player> teamBPlayers = session.getTeamB().getPlayers();


        // Set winning team in the session
        if(team.equals(Team.A)){



            session.setWinningTeam(team);
            sessionRepository.save(session);
            for (Player player : teamAPlayers) {
                player.setSessionsPlayed(player.getSessionsPlayed() + 1);
                player.setSessionsWon(player.getSessionsWon() + 1);
                player.getPastSessions().put(session, true); // true if A won
                playerRepository.save(player);
            }
            for (Player player : teamBPlayers) {
                player.setSessionsPlayed(player.getSessionsPlayed() + 1);
                player.setSessionsLost(player.getSessionsLost() + 1);
                player.getPastSessions().put(session, false); // false if A lost
                playerRepository.save(player);
            }

        }
        else {

            session.setWinningTeam(team);
            sessionRepository.save(session);
            for (Player player : teamAPlayers) {
                player.setSessionsPlayed(player.getSessionsPlayed() + 1);
                player.setSessionsLost(player.getSessionsLost() + 1);
                player.getPastSessions().put(session, false); // true if A won
                playerRepository.save(player);
            }
            for (Player player : teamBPlayers) {
                player.setSessionsPlayed(player.getSessionsPlayed() + 1);
                player.setSessionsWon(player.getSessionsWon() + 1);
                player.getPastSessions().put(session, true); // false if A lost
                playerRepository.save(player);
            }



        }


    }
    public List<SessionDTO> getSessionsByRefereeId(long refereeId) {
        // Fetch the referee by ID
        User referee = userService.getUserById(refereeId);

        // Validate if the user is indeed a referee
        if (referee.getRole() != Role.ROLE_REFEREE) {
            throw new UserNotFoundException("User with ID " + refereeId + " is not a referee.");
        }

        // Fetch all sessions where this referee is assigned
        List<Session> sessions = sessionRepository.findByReferee(referee);

        // If no sessions found for this referee
        if (sessions.isEmpty()) {
            throw new SessionNotFoundException("No sessions found for referee with ID " + refereeId);
        }

        // Sort sessions by matchDate then matchStartTime
        sessions.sort(Comparator
                .comparing(Session::getMatchDate)
                .thenComparing(Session::getMatchStartTime));

        // Convert to DTOs
        return sessions.stream()
                .map(SessionDTO::new)
                .collect(Collectors.toList());
    }
    public void chooseMvp(Long sessionId, Long playerId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found with ID: " + sessionId));

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
