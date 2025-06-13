package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.InvitationDTO;
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
import java.util.*;
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

    @Autowired
    private  UserService userService;
    List<Player> pl= new ArrayList<>();
    @Transactional
    public void invitePlayersToTeam(Long sessionId,Team team, Long playerId, Long receiverId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session " + sessionId + " not found "));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player " + playerId + " not found "));
            Team t;
        SessionTeam sessionTeam ;
        if(team==Team.A){
            sessionTeam=session.getTeamA();
            t=Team.A;
        }
        else{
            sessionTeam=session.getTeamB();
            t=Team.B;
        }



        if (!session.getType().equals(SessionType.Teams)) {
            throw new WrongInvitationTypeException("Session type doesn't match Invitation type");
        }


            Player receiver = playerRepository.findById(receiverId)
                    .orElseThrow(() -> new PlayerNotFoundException("receiver " + receiverId + " not found"));

        Optional<TeamInvitation> existingInvite = teamInvitationRepository
                .findBySessionAndReceiverAndStatus(session, receiver, InviteStatus.PENDING);

        if (existingInvite.isPresent()) {
            throw new InvitationSentException("An invite has already been sent to this player for this session.");
        }


            // Create new invite
            TeamInvitation invite = new TeamInvitation();
            LocalDateTime time = LocalDateTime.now();
            invite.setTeam(sessionTeam);
            invite.setTeamName(t);
            invite.setPlayer(player);
            invite.setReceiver(receiver);
            invite.setSession(session);
            invite.setStatus(InviteStatus.PENDING);
            invite.setCreatedAt(time);
            teamInvitationRepository.save(invite);



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
        if (invite.getSession().getWinningTeam() != null) {
            throw new SessionFinalizedException("This session has already been finalized.");
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

            checkAndConfirmTeam( invite.getSession(),invite.getTeam(),pl,invite.getPlayer());
        }
        if(status == InviteStatus.DECLINED){
            invite.setStatus(InviteStatus.DECLINED);
        }
    }
    @Transactional
    public void checkAndConfirmTeam(Session session,SessionTeam team, List<Player> receivers,Player player) {
        List<TeamInvitation> acceptedInvites = teamInvitationRepository.findByTeamAndStatus(team, InviteStatus.ACCEPTED);

        if (acceptedInvites.size() == 4) {
            // First, fetch the team to ensure it's loaded




            for (Player p : receivers) {
                // Get managed version of player
                Player managedPlayer = playerRepository.findById(p.getId())
                        .orElseThrow(() -> new PlayerNotFoundException("Player not found"));


                // Add player to team (if not already there)
                if (!team.getPlayers().contains(managedPlayer)) {
                    team.getPlayers().add(managedPlayer);
                }
                managedPlayer.getSessionTeams().add(team);


                // Add team to player using JPA merge to avoid lazy init issues
                // This avoids directly accessing the sessionTeams collection
                // and relies on the database to maintain relationship consistency
                 playerRepository.save(managedPlayer);
            }
            player.getSessionTeams().add(team);
            playerRepository.save(player);

            // Update session's player count
            session.setPlayerCount(team.getPlayers().size());
            for(Player p : team.getPlayers()) {
                System.out.println(p.getId());
            }

            // Save the team
            sessionTeamRepository.save(team);

            // Save the session
            sessionRepository.save(session);
        }
    }
    @Transactional
    public List<Player> getConfirmedTeamPlayers(Long sessionId, Team team, Long playerId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session " + sessionId + " not found"));

        SessionTeam sessionTeam = (team == Team.A) ? session.getTeamA() : session.getTeamB();

        Player inviter = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player " + playerId + " not found"));

        // Get all accepted invites for this team in the session where the inviter is this player
        List<TeamInvitation> acceptedInvites = teamInvitationRepository
                .findBySessionAndTeamAndPlayerAndStatus(session, sessionTeam, inviter, InviteStatus.ACCEPTED);

        Set<Player> players = new LinkedHashSet<>();
        players.add(inviter); // Always include the inviter

        for (TeamInvitation invite : acceptedInvites) {
            players.add(invite.getReceiver()); // Add only accepted receivers
        }

        return new ArrayList<>(players);
    }
    public  InvitationDTO getTeamInviteById(Long inviteId) {
        TeamInvitation invite = teamInvitationRepository.findById(inviteId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation with id " + inviteId + " not found"));

        Player sender = invite.getPlayer();
        String senderUsername = userService.getUserByPlayerId(sender.getId()).getUsername();
         boolean t;
            if(invite.getStatus() == InviteStatus.ACCEPTED) {

                 t =true;
            }
            else{
                 t =false;
            }
        return new InvitationDTO(
                invite.getId(),
                invite.getSession().getId(),
                invite.getSession().getMatchDate(),
                senderUsername,
               t,
                invite.getCreatedAt(),
                invite.getSession().getType()
        );
    }



}
