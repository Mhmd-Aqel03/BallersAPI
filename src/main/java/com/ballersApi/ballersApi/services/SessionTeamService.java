package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionTeam;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionTeamService {
    @Autowired
    private SessionTeamRepository sessionTeamRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private PlayerRepository playerRepository;
    public SessionTeam createSession(SessionTeam session) {
        return sessionTeamRepository.save(session);
    }
    @Transactional
    public SessionTeam createTeamSession(Long sessionId) {
        try {
            // Validate session existence
            Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
            if (sessionOpt.isEmpty()) {
                throw new IllegalArgumentException("Session not found with ID: " + sessionId);
            }
            Session session = sessionOpt.get();

            // Ensure session does not exceed 2 teams
            List<SessionTeam> existingTeams = sessionTeamRepository.findBySession(session);
            if (existingTeams.size() >= 2) {
                throw new IllegalStateException("This session already has two teams assigned.");
            }


            SessionTeam teamSession = new SessionTeam(session);
            return sessionTeamRepository.save(teamSession);

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Validation Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            return null;
        }
    }

    public SessionTeam joinTeamSession(Long teamSessionId, Long playerId) {
        try {
            // Validate team session existence
            Optional<SessionTeam> teamSessionOpt = sessionTeamRepository.findById(teamSessionId);
            if (teamSessionOpt.isEmpty()) {
                throw new IllegalArgumentException("Team session not found with ID: " + teamSessionId);
            }
            SessionTeam teamSession = teamSessionOpt.get();

            // Validate player existence
            Optional<Player> playerOpt = playerRepository.findById(playerId);
            if (playerOpt.isEmpty()) {
                throw new IllegalArgumentException("Player not found with ID: " + playerId);
            }
            Player player = playerOpt.get();

            // Ensure team does not exceed max players (5)
            if (teamSession.getPlayers().size() >= 5) {
                throw new IllegalStateException("Team is already full (max 5 players).");
            }

            // Add player to the team and save
            teamSession.getPlayers().add(player);
            return sessionTeamRepository.save(teamSession);

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Validation Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            return null;
        }
    }

   public void deleteAllTeamSessions() {
        try {
            if (sessionTeamRepository.findAll().isEmpty()) {
                throw new IllegalArgumentException("Team sessions do not exist. " );
            }
            sessionTeamRepository.deleteAll();
        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        }
    }
    public List<SessionTeam> getTeamsBySession(Long sessionId) {
        try {
            // Validate session existence
            Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
            if (sessionOpt.isEmpty()) {
                throw new IllegalArgumentException("Session not found with ID: " + sessionId);
            }
            return sessionTeamRepository.findBySession(sessionOpt.get());

        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error: " + e.getMessage());
            return List.of(); // Return empty list
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            return List.of(); // Return empty list
        }
    }





}
