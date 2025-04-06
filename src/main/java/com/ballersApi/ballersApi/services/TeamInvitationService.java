package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.*;
import com.ballersApi.ballersApi.models.*;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import com.ballersApi.ballersApi.repositories.TeamInvitationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamInvitationService {
    @Autowired
    private TeamInvitationRepository teamInvitationRepository;
    @Autowired
    private SessionTeamRepository sessionTeamRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private PlayerRepository playerRepository;

    List<Player> pl= new ArrayList<>();
    @Transactional
    public void invitePlayersToTeam(Long sessionId,Long teamId, Long playerId, List<Long> receiverId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session " + sessionId + " not found "));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player " + playerId + " not found "));

        SessionTeam sessionTeam = sessionTeamRepository.findById(teamId)
                .orElseThrow(()-> new TeamSessionNotFoundException("Team " + teamId + " not found "));

        if (receiverId.size() < 4) {
            throw new NotEnoughPlayersException("You must invite at least 4 players to form a team");
        }

        boolean isInSession = player.getSessionTeams().stream()
                .anyMatch(team -> team.getSession().getId().equals(sessionId));

        if (!isInSession) {
            throw new PlayerNotFoundException("Player "+ playerId + " is not in session");
        }
        if(!session.getType().equals(SessionType.Teams)){
            throw new WrongInvitationTypeException("Session type doesn't match Invitation type");
        }
        List<Player> receivers = new ArrayList<>();
        for (Long rId : receiverId) {
            Player receiver = playerRepository.findById(rId)
                    .orElseThrow(() -> new PlayerNotFoundException("receiver " + rId + " not found"));

            // Check if a receiver already exists
            teamInvitationRepository.findBySessionAndReceiver(session, receiver)
                    .ifPresent(invite -> teamInvitationRepository.delete(invite)); // Remove old invite if re-inviting
            receivers.add(receiver);
            // Create new invite
            TeamInvitation invite = new TeamInvitation();
            LocalDateTime time = LocalDateTime.now();
            invite.setTeam(sessionTeam);
            invite.setPlayer(player);
            invite.setReceivers(receivers);
            invite.setReceiver(receiver);
            invite.getTeam().setSession(session);
            invite.setStatus(InviteStatus.PENDING);
            invite.setCreatedAt(time);
            teamInvitationRepository.save(invite);
        }
    }

    @Transactional
    public void respondToInvite(Long inviteId, InviteStatus status) {
        TeamInvitation invite = teamInvitationRepository.findById(inviteId)
                .orElseThrow(() -> new InvitationNotFoundException("Invite " + inviteId + " not found"));
        List<TeamInvitation> acceptedInvites = teamInvitationRepository.findByTeamAndStatus(invite.getTeam(), InviteStatus.ACCEPTED);

        if (acceptedInvites.size() >= 4) {
            throw new TeamFullException("This team is already full. You cannot join.");
        }
        if (invite.getStatus() != InviteStatus.PENDING) {
            throw new RespondedToInviteException("You have already responded to this invite.");
        }


        invite.setStatus(status);
        teamInvitationRepository.save(invite);

        if (status == InviteStatus.ACCEPTED) {
            boolean alreadyInTeam = pl.stream()
                    .anyMatch(player -> player.getId().equals(invite.getReceiver()
                            .getId()));
            if (alreadyInTeam) {
                throw new PlayerAlreadyInTeamException("You are already in the team.");
            }
            pl.add(invite.getReceiver());
            checkAndConfirmTeam( invite.getTeam(),pl);
        }
        if(status == InviteStatus.DECLINED){
            invite.setStatus(InviteStatus.DECLINED);
        }
    }
    @Transactional
    public void checkAndConfirmTeam(SessionTeam team, List<Player> receivers) {
        List<TeamInvitation> acceptedInvites = teamInvitationRepository.findByTeamAndStatus(team, InviteStatus.ACCEPTED);

        if (acceptedInvites.size() >= 4) {
            // First, fetch the team to ensure it's loaded
            SessionTeam managedTeam = sessionTeamRepository.findById(team.getId())
                    .orElseThrow(() -> new TeamSessionNotFoundException("Team not found"));

            for (Player p : receivers) {
                // Get managed version of player
                Player managedPlayer = playerRepository.findById(p.getId())
                        .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

                // Add player to team (if not already there)
                if (!managedTeam.getPlayers().contains(managedPlayer)) {
                    managedTeam.getPlayers().add(managedPlayer);
                }
                managedPlayer.getSessionTeams().add(managedTeam);

                // Add team to player using JPA merge to avoid lazy init issues
                // This avoids directly accessing the sessionTeams collection
                // and relies on the database to maintain relationship consistency
                 playerRepository.save(managedPlayer);
            }

            // Update session's player count
            managedTeam.getSession().setPlayerCount(managedTeam.getPlayers().size());
            for(Player p : managedTeam.getPlayers()) {
                System.out.println(p.getId());
            }

            // Save the team
            sessionTeamRepository.save(managedTeam);

            // Save the session
            sessionRepository.save(managedTeam.getSession());
        }
    }
}
