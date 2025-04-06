package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.SessionTeamDTO;
import com.ballersApi.ballersApi.exceptions.*;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionTeam;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        // Validate session existence
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            throw new SessionNotFoundException("Session with id " + sessionId + " not found");
        }
        Session session = sessionOpt.get();

        // Ensure session does not exceed 2 teams
        List<SessionTeam> existingTeams = sessionTeamRepository.findBySession(session);
        if (existingTeams.size() >= 2) {
            throw new TeamSessionCreationException("This session already has two teams assigned.");
        }


        SessionTeam teamSession = new SessionTeam(session);
        return sessionTeamRepository.save(teamSession);
    }

    public SessionTeam joinTeamSession(Long teamSessionId, Long playerId) {

        // Validate team session existence
        Optional<SessionTeam> teamSessionOpt = sessionTeamRepository.findById(teamSessionId);
        if (teamSessionOpt.isEmpty()) {

            throw new TeamSessionNotFoundException("Team Session with id " + teamSessionId + " not found");
        }

        SessionTeam teamSession = teamSessionOpt.get();

        // Validate player existence
        Optional<Player> playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isEmpty()) {
            throw new PlayerNotFoundException("Player with id " + playerId + " not found");
        }
        List<SessionTeam> teamsInSession = sessionTeamRepository.findBySessionId(teamSession.getSession().getId());
        boolean isInAnyTeamInSession = teamsInSession.stream()
                .anyMatch(team -> team.getPlayers().stream()
                        .anyMatch(playerInTeam -> playerInTeam.getId().equals(playerId)));


        Player player = playerOpt.get();
        if (isInAnyTeamInSession) {
            throw new PlayerAlreadyInTeamException("Player already joined team  with ID: " + teamSessionId);
        }


        // Ensure team does not exceed max players (5)
        if (!(teamSession.getSession().getPlayerCount() < teamSession.getSession().getMaxPlayers())) {
            throw new TeamFullException("Team is already full.");
        }

        // Add player to the team and save
        teamSession.getPlayers().add(player);
        player.getSessionTeams().add(teamSession);
        teamSession.getSession().setPlayerCount(teamSession.getSession().getPlayerCount() + 1);
        playerRepository.save(player);

        return sessionTeamRepository.save(teamSession);


    }

    public void leaveTeam(Long playerId, Long teamId) {
        // Find the player
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player " + playerId + " not found "));

        // Find the team
        SessionTeam teamSession = sessionTeamRepository.findById(teamId)
                .orElseThrow(() -> new TeamSessionNotFoundException("Team " + teamId + " not found"));

        // Check if the player is in the team
        if (!teamSession.getPlayers().contains(player)) {
            throw new PlayerNotInTeamException("Player id: " + playerId + " is not part of this team");
        }

        // Remove the player from the team
        teamSession.getPlayers().remove(player);
        player.getSessionTeams().remove(teamSession);
        teamSession.getSession().setPlayerCount(teamSession.getSession().getPlayerCount() - 1);
        playerRepository.save(player);

        // Save the updated team
        sessionTeamRepository.save(teamSession);
    }

    @Transactional
    public void deleteAllTeamSessions(long id) {
        try {
            sessionTeamRepository.deleteSessionTeamBySessionId(id);
        } catch(DataAccessException e){
            throw new DatabaseConnectionErrorException("something went wrong while trying to delete session teams: " + e.getMessage());
        }
    }

    public List<SessionTeamDTO> getTeamsBySession(Long sessionId) {
        // Validate session existence
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            throw new SessionNotFoundException("Session with id " + sessionId + " not found");
        }

        // Get teams for this session
        List<SessionTeam> teams = sessionTeamRepository.findBySession(sessionOpt.get());
        for (Player p : teams.get(0).getPlayers()) {
            System.out.println(p.getId());
        }
        // Map each team to a DTO
        List<SessionTeamDTO> teamDTOs = new ArrayList<>();
        for (SessionTeam team : teams) {
            // Debug line to check if teams have players
            System.out.println("Team " + team.getId() + " has " +
                    (team.getPlayers() != null ? team.getPlayers().size() : 0) + " players");

            teamDTOs.add(new SessionTeamDTO(team));
        }

        return teamDTOs;
    }


}
