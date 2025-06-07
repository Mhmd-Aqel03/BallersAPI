package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.InvitationDTO;
import com.ballersApi.ballersApi.exceptions.*;
import com.ballersApi.ballersApi.models.Invitation;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionType;
import com.ballersApi.ballersApi.repositories.InvitationRepository;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {
    @Autowired
    private final InvitationRepository invitationRepository;
    @Autowired
    private final PlayerRepository playerRepository;
    @Autowired
    private final SessionRepository sessionRepository;

    @Autowired
    private final UserService userService;
    public String sendInvite(Long playerId, Long receiverId, Long sessionId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id " + playerId + " not found"));
        Player receiver = playerRepository.findById(receiverId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id " + receiverId + " not found"));
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new TeamSessionNotFoundException("Team session with id " + sessionId + " not found"));

        if (!session.getType().equals(SessionType.Random)) {
            throw new WrongInvitationTypeException("Session type doesn't match Invitation type");
        }

        Invitation invite = new Invitation();
        invite.setPlayer(player);
        invite.setReceiver(receiver);
        invite.setSession(session);
        invite.setCreatedAt(LocalDateTime.now());
        invite.setStatus(false); // New invite is pending

        invitationRepository.save(invite);

        return "Invitation sent successfully to " + userService.getUserById(receiver.getId()).getUsername();
    }

    public Session respondToInvite(Long inviteId, boolean status) {
        Invitation invite = invitationRepository.findById(inviteId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation with id " + inviteId + " not found"));
        if (invite.getSession().getWinningTeam() != null) {
            throw new SessionFinalizedException("This session has already been finalized.");
        }


        invite.setStatus(status);
        invitationRepository.save(invite);

        if (status) {
           return invite.getSession();
        }
        else {
            return null;
        }
    }


    public List<InvitationDTO> getIncomingInvitesForReceiver(Long receiverId) {
        List<Invitation> invites = invitationRepository.findByReceiverId(receiverId);
        if (invites.isEmpty()) {
            throw new NoInvitationsFoundException("No invites found for this player.");
        }

        boolean allAccepted = invites.stream().allMatch(Invitation::isStatus);
        if (allAccepted) {
            throw new NoInvitationsFoundException("No invites found for this player.");
        }

        return invites.stream().filter(invite -> !invite.isStatus())
                .map(invite -> {
            Player sender = invite.getPlayer();
            String senderUsername = userService.getUserByPlayerId(sender.getId()).getUsername();

            return new InvitationDTO(
                    invite.getId(),
                    invite.getSession().getId(),
                    invite.getSession().getMatchDate(),
                    senderUsername,
                    invite.isStatus(),
                    invite.getCreatedAt()
            );
        }).collect(Collectors.toList());
    }


    public InvitationDTO getInviteById(Long inviteId) {
        Invitation invite = invitationRepository.findById(inviteId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation with id " + inviteId + " not found"));

        Player sender = invite.getPlayer();
        String senderUsername = userService.getUserByPlayerId(sender.getId()).getUsername();

        return new InvitationDTO(
                invite.getId(),
                invite.getSession().getId(),
                invite.getSession().getMatchDate(),
                senderUsername,
                invite.isStatus(),
                invite.getCreatedAt()
        );
    }

}
