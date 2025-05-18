package com.ballersApi.ballersApi.services;

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

@Service
@RequiredArgsConstructor
public class InvitationService {
    @Autowired
    private final InvitationRepository invitationRepository;
    @Autowired
    private final PlayerRepository playerRepository;
    @Autowired
    private final SessionRepository sessionRepository;

    public Invitation sendInvite(Long playerId, Long receiverId, Long sessionId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id " + playerId + " not found"));
        Player receiver = playerRepository.findById(receiverId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id " + receiverId + " not found"));
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new TeamSessionNotFoundException("Team session with id " + sessionId + " not found"));

        boolean isInSession = player.getSessionTeams().stream()
                .anyMatch(team1 -> session.getId().equals(sessionId));

       
        if(!session.getType().equals(SessionType.Random)){
            throw new WrongInvitationTypeException("Session type doesn't match Invitation type");
        }



        Invitation invite = new Invitation();
        LocalDateTime time = LocalDateTime.now();
        invite.setPlayer(player);
        invite.setReceiver(receiver);
        invite.setSession(session);
        invite.setCreatedAt(time);
        invite.setStatus(false); // New invite is pending

        return invitationRepository.save(invite);
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

}
