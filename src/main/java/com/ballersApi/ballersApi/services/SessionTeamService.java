package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.SessionTeamDTO;
import com.ballersApi.ballersApi.exceptions.*;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionTeam;
import com.ballersApi.ballersApi.models.Team;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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
    @Autowired
    private UserService userService;

    @Transactional
    public SessionTeam createTeamSession(Long sessionId) {

            // Validate session existence
            Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
            if (sessionOpt.isEmpty()) {
                throw new SessionNotFoundException("Session with id " + sessionId + " not found");
            }
            Session session = sessionOpt.get();

            // Ensure session does not exceed 2 teams

            if (session.getTeamA() != null&&session.getTeamB() != null) {
                throw new TeamSessionCreationException("This session already has two teams assigned.");
            }

            SessionTeam teamSession = new SessionTeam();
        if(session.getTeamA() == null) {
            session.setTeamA(teamSession);
        }
        else {
            session.setTeamB(teamSession);
        }
        sessionRepository.save(session);
            return sessionTeamRepository.save(teamSession);
    }

    public SessionTeamDTO joinTeamSession(Long sessionId, Long playerId, Team team) {

            // Validate team session existence
            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
            if (sessionOptional.isEmpty()) {

                throw new SessionNotFoundException("Team Session with id " + sessionId + " not found");
            }

            Session session = sessionOptional.get();

        if (session.getWinningTeam() != null) {
            throw new SessionFinalizedException("This session has already been finalized.");
        }
            // Validate player existence
            Optional<Player> playerOpt = playerRepository.findById(playerId);
            if (playerOpt.isEmpty()) {
                throw new PlayerNotFoundException("Player with id " + playerId + " not found");
            }
        List<SessionTeam> teamsInSession = new ArrayList<>();
            teamsInSession.add(session.getTeamA());
            teamsInSession.add(session.getTeamB());

        boolean isInAnyTeamInSession = false;
        SessionTeam teamS =null;

        for (SessionTeam team1 : teamsInSession) {

            boolean playerInTeam = team1.getPlayers().stream()
                    .anyMatch(playerInT -> playerInT.getId().equals(playerId));

            if (playerInTeam) {
                isInAnyTeamInSession = true;
                teamS = team1;
                break;
            }
        }


        Player player = playerOpt.get();
            if (isInAnyTeamInSession) {
                throw new PlayerAlreadyInTeamException("Player already joined team  with ID: " + teamS.getId());
            }

        if ( LocalDate.now().isAfter(session.getMatchDate())) {
            throw new SessionNotFoundException("You cannot join a session that has already finished.");
        }

        if (LocalTime.now().isAfter(session.getMatchEndTime())&& LocalDate.now().isEqual(session.getMatchDate())) {
            throw new SessionNotFoundException("You cannot join a session that has already finished.");
        }



            if (!(session.getPlayerCount() < session.getMaxPlayers())) {
                throw new TeamFullException("Team is already full.");
            }


                if(team.equals(Team.A)) {

                    session.getTeamA().getPlayers().add(player);
                    player.getSessionTeams().add(session.getTeamA());

                    session.setPlayerCount(session.getPlayerCount()+1);
                    playerRepository.save(player);
                    sessionRepository.save(session);
                    sessionTeamRepository.save(session.getTeamA());
                 return new SessionTeamDTO(session.getTeamA(), Team.A,userService);

                }
                else {
                    session.getTeamB().getPlayers().add(player);
                    player.getSessionTeams().add(session.getTeamB());

                    session.setPlayerCount(session.getPlayerCount()+1);
                    playerRepository.save(player);
                    sessionRepository.save(session);
                     sessionTeamRepository.save(session.getTeamB());
                   return new SessionTeamDTO(session.getTeamB(), Team.B,userService);




                }



    }
    public void leaveTeam(Long playerId, Long sessionId, Team team) {
        // Find the player
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player "+playerId+" not found "));

        // Find the team
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session "+sessionId+" not found"));
        SessionTeam sessionTeam ;
        if(team == Team.A) {
             sessionTeam = session.getTeamA();
        }
        else {
             sessionTeam = session.getTeamB();
        }

        if (!sessionTeam.getPlayers().contains(player)) {
            throw new PlayerNotInTeamException("Player id: "+ playerId + " is not part of this team");
        }
        if ( LocalDate.now().isAfter(session.getMatchDate())) {
            throw new SessionNotFoundException("You cannot join a session that has already finished.");
        }

        if (LocalTime.now().isAfter(session.getMatchEndTime())&& LocalDate.now().isEqual(session.getMatchDate())) {
            throw new SessionNotFoundException("You cannot join a session that has already finished.");
        }

        // Remove the player from the team
        sessionTeam.getPlayers().remove(player);

        player.getSessionTeams().remove(sessionTeam);
       session.setPlayerCount(session.getPlayerCount()-1);
        playerRepository.save(player);

        // Save the updated team
        sessionTeamRepository.save(sessionTeam);
    }

    public void deleteAllTeamSessions(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session "+sessionId+" not found"));

            List<SessionTeam> teamsInSession = new ArrayList<>();
            teamsInSession.add(session.getTeamA());
            teamsInSession.add(session.getTeamB());

            if (teamsInSession.isEmpty()) {
                throw new TeamSessionNotFoundException("No teams found for session with ID: " + sessionId);
            }

            sessionTeamRepository.deleteAll(teamsInSession);

    }
    public List<SessionTeamDTO> getTeamsBySession(Long sessionId) {
        // Validate session existence
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            throw new SessionNotFoundException("Session with id " + sessionId + " not found");
        }
            Session session = sessionOpt.get();
        // Get teams for this session
        List<SessionTeam> teams = new ArrayList<>();
        teams.add(session.getTeamA());
        teams.add(session.getTeamB());
        for(Player p : teams.get(0).getPlayers()) {
            System.out.println(p.getId());

        }
        // Map each team to a DTO
        List<SessionTeamDTO> teamDTOs = new ArrayList<>();
        for (SessionTeam team : teams) {
            // Debug line to check if teams have players
            System.out.println("Team " + team.getId() + " has " +
                    (team.getPlayers() != null ? team.getPlayers().size() : 0) + " players");
                    if(session.getTeamA().equals(team)) {
                        teamDTOs.add(new SessionTeamDTO(team,Team.A,userService));
                    }
                    else if(session.getTeamB().equals(team)) {
                        teamDTOs.add(new SessionTeamDTO(team,Team.B,userService));
                    }

        }

        return teamDTOs;
    }




}
