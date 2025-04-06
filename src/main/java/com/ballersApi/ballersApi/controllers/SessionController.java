package com.ballersApi.ballersApi.controllers;
import com.ballersApi.ballersApi.dataTransferObjects.SessionTeamDTO;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionTeam;
import com.ballersApi.ballersApi.services.SessionService;
import com.ballersApi.ballersApi.services.SessionTeamService;
import com.ballersApi.ballersApi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController


@RequestMapping("/session")

public class SessionController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionTeamService sessionTeamService;

    @Autowired
    private UserService userService;
    

    @GetMapping("getSessions")
    public ResponseEntity<Map<String,Object>> getAllUpcomingSessions(){
        Map<String, Object> response = new HashMap<>();

        response.put("sessions", sessionService.getAllUpcomingSessions());

        return ResponseEntity.ok(response);

    }

    @GetMapping("getSession/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable long id){
        return sessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
    @GetMapping("/getSessionsForWeek")
    public Map<String, List<Session>> getSessionsForWeek(@RequestParam LocalDate Date) {
        return sessionService.getSessionsForWeek(Date);
    }

    @PostMapping("createSession")
    public SessionTeam createSession(@Valid @RequestBody Session session){

        sessionService.createSession(session);
        sessionTeamService.createTeamSession(session.getId());
        return sessionTeamService.createTeamSession(session.getId());


    }
    @DeleteMapping("deleteSession/{id}")

    public ResponseEntity<Void> deleteSession(@PathVariable  Long id){

        sessionTeamService.deleteAllTeamSessions();
        sessionService.deleteSession(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("joinSessionTeam/{teamSessionId}")
    public ResponseEntity<SessionTeam> joinTeamSession(@PathVariable Long teamSessionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getId();
        SessionTeam teamSession = sessionTeamService.joinTeamSession(teamSessionId, playerId);

  

        if (teamSession != null) {
            return ResponseEntity.ok(teamSession);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("leaveSessionTeam/{teamId}")
    public ResponseEntity<String> leaveTeam(@PathVariable Long teamId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getId();
        sessionTeamService.leaveTeam(playerId, teamId);
        return ResponseEntity.ok("Player removed from the team successfully");
    }
    @DeleteMapping("/deleteSessionTeam/{id}")
    public ResponseEntity<Void> deleteTeamSession() {
        sessionTeamService.deleteAllTeamSessions();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getTeam/{sessionId}")
    public ResponseEntity<List<SessionTeamDTO>> getTeamsBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionTeamService.getTeamsBySession(sessionId));
    }
}
