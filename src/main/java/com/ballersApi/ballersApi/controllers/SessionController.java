package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.dataTransferObjects.SessionTeamDTO;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.Team;
import com.ballersApi.ballersApi.services.SessionService;
import com.ballersApi.ballersApi.services.SessionTeamService;
import com.ballersApi.ballersApi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController


@RequestMapping("/Session")
public class SessionController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionTeamService sessionTeamService;
    @Autowired
    private UserService userService;
    @GetMapping("getAllSessions")

    public List<Session> getAllUpcomingSessions(){
        return sessionService.getAllUpcomingSessions();
    }

    @GetMapping("getSession/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable long id){
        return sessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
    @GetMapping("/getSessionsForWeek")
    public ResponseEntity<Map<String, Object>> getSessionsForWeek(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Map<String, List<Session>> sessions = sessionService.getSessionsForWeek(date);
        Map<String, Object> response = new HashMap<>();
        response.put("data", sessions);
        return ResponseEntity.ok(response);
    }

    @PostMapping("joinSessionTeam/{sessionId}/{team}")
    public ResponseEntity<SessionTeamDTO> joinTeamSession(@PathVariable Long sessionId, @PathVariable Team team) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getId();
        SessionTeamDTO teamSession = sessionTeamService.joinTeamSession(sessionId, playerId, team);

        if (teamSession != null) {
            return ResponseEntity.ok(teamSession);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("leaveSessionTeam/{sessionId}/{team}")
    public ResponseEntity<String> leaveTeam(@PathVariable Long sessionId,@PathVariable Team team) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getId();
        sessionTeamService.leaveTeam(playerId, sessionId, team);
        return ResponseEntity.ok("Player removed from the team successfully");
    }
    
    @GetMapping("/getTeam/{sessionId}")
    public ResponseEntity<List<SessionTeamDTO>> getTeamsBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionTeamService.getTeamsBySession(sessionId));
    }




}
