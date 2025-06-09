package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.dataTransferObjects.InvitationDTO;
import com.ballersApi.ballersApi.models.Invitation;
import com.ballersApi.ballersApi.models.InviteStatus;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.Team;
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
    @PostMapping("/Invite/{receiverId}/{sessionId}")
    public ResponseEntity<String> sendInvite(
            @PathVariable Long receiverId,
            @PathVariable Long sessionId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getPlayer().getId();

        // Call the service to send the invite and get success message
        String successMessage = invitationService.sendInvite(playerId, receiverId, sessionId);
        return ResponseEntity.ok(successMessage);
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
    @PostMapping("/TeamInvite/{sessionId}/{team}")
    public ResponseEntity<String> invitePlayersToTeam(@PathVariable Long sessionId,
                                                @PathVariable Team team,
                                                @RequestBody List<Long> receiverIds) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getPlayer().getId();
        teamInvitationService.invitePlayersToTeam(sessionId ,team,playerId, receiverIds);
        return ResponseEntity.ok("Team invites sent successfully");
    }
    @PostMapping("/TeamRespond/{inviteId}/")
    public ResponseEntity<String> respondToInvite(@PathVariable Long inviteId,

                                                  @RequestParam InviteStatus status) {
        teamInvitationService.respondToInvite(inviteId ,status);
        return ResponseEntity.ok("Invite response updated");
    }
    @GetMapping("/received/")
    public ResponseEntity<List<InvitationDTO>> getIncomingInvites() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long receiverId = userService.getUserByUsername(username).getPlayer().getId();
        List<InvitationDTO> invitations = invitationService.getIncomingInvitesForReceiver(receiverId);
        return ResponseEntity.ok(invitations);
    }
    @GetMapping("/invite/{id}")
    public ResponseEntity<InvitationDTO> getInviteById(@PathVariable Long id) {
        InvitationDTO dto = invitationService.getInviteById(id);
        return ResponseEntity.ok(dto);
    }
}
