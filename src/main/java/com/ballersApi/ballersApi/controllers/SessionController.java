package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionTeam;
import com.ballersApi.ballersApi.services.SessionService;
import com.ballersApi.ballersApi.services.SessionTeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("getSessions")
    public ResponseEntity<Map<String,Object>> getAllUpcomingSessions(){
        Map<String, Object> response = new HashMap<>();

        response.put("sessions", sessionService.getAllUpcomingSessions());

        return ResponseEntity.ok(response);
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

    @PostMapping("createSessionTeam")
    public ResponseEntity<SessionTeam> createTeamSession(@RequestParam Long sessionId) {
        SessionTeam teamSession = sessionTeamService.createTeamSession(sessionId);
        if (teamSession != null) {
            return ResponseEntity.ok(teamSession);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("joinSessionTeam")
    public ResponseEntity<SessionTeam> joinTeamSession(@RequestParam Long teamSessionId, @RequestParam Long playerId) {
        SessionTeam teamSession = sessionTeamService.joinTeamSession(teamSessionId, playerId);
        if (teamSession != null) {
            return ResponseEntity.ok(teamSession);
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/deleteSessionTeam/{id}")
    public ResponseEntity<Void> deleteTeamSession() {
        sessionTeamService.deleteAllTeamSessions();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getTeam/{sessionId}")
    public ResponseEntity<List<SessionTeam>> getTeamsBySession(@PathVariable Long sessionId) {
        List<SessionTeam> teams = sessionTeamService.getTeamsBySession(sessionId);
        return ResponseEntity.ok(teams);
    }




}
