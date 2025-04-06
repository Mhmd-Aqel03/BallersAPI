package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.Invitation;
import com.ballersApi.ballersApi.models.InviteStatus;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.services.InvitationService;
import com.ballersApi.ballersApi.services.PlayerAuthService;
import com.ballersApi.ballersApi.services.TeamInvitationService;
import com.ballersApi.ballersApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Invitation")
public class InvitationController {
    @Autowired
    private  InvitationService invitationService;
    @Autowired
    private TeamInvitationService teamInvitationService;
    @Autowired
    private UserService userService ;


    // Send an Invitation to a player
    @PostMapping("/send/{receiverId}/{sessionId}")
    public ResponseEntity<Invitation> sendInvite(

            @PathVariable Long receiverId,
            @PathVariable Long sessionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getId();

        // Call the service to send the invite
        Invitation invite = invitationService.sendInvite(playerId, receiverId, sessionId);
        return ResponseEntity.ok(invite); // Return the invite details
    }

    @PostMapping("/respond/{inviteId}/{status}")
    public ResponseEntity<Session> respondToInvite(
            @PathVariable Long inviteId,
            @PathVariable boolean status) {

        // Call the service to respond to the invite and get the session if accepted
        Session session = invitationService.respondToInvite(inviteId, status);

        // If the invite is accepted, return the session
        if (session != null) {
            return ResponseEntity.ok(session); // Return the session if accepted
        } else {
            return ResponseEntity.noContent().build(); // Return no content (HTTP 204) if declined
        }


    }
    @PostMapping("/TeamInvite/{sessionId}/{teamId}")
    public ResponseEntity<String> invitePlayersToTeam(@PathVariable Long sessionId,
                                                @PathVariable Long teamId,
                                                @RequestBody List<Long> receiverIds) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getId();
        teamInvitationService.invitePlayersToTeam(sessionId ,teamId,playerId, receiverIds);
        return ResponseEntity.ok("Team invites sent successfully");
    }
    @PostMapping("/TeamRespond/{inviteId}/")
    public ResponseEntity<String> respondToInvite(@PathVariable Long inviteId,

                                                  @RequestParam InviteStatus status) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Long playerId = userService.getUserByUsername(username).getId();
        teamInvitationService.respondToInvite(inviteId ,status);
        return ResponseEntity.ok("Invite response updated");
    }
}
