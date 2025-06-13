package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.dataTransferObjects.AllPlayersDTO;
import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.dataTransferObjects.SessionTeamDTO;
import com.ballersApi.ballersApi.models.Player;
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
import java.util.stream.Collectors;

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
    public List<SessionDTO> getAllUpcomingSessions() {
        return sessionService.getAllUpcomingSessions()
                .stream()
                .map(session -> new SessionDTO(session, userService))  // inject userService here
                .collect(Collectors.toList());
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
    @PostMapping("createSession")
    public Session createSession(@Valid @RequestBody SessionDTO request) {
        Session session = new Session();
        session.setType(request.getType());
        session.setMatchDate(request.getMatchDate());
        session.setMatchStartTime(request.getMatchStartTime());
        session.setMatchEndTime(request.getMatchEndTime());
        session.setMaxPlayers(request.getMaxPlayers());
        session.setPrice(request.getPrice());
        session.setPlayerCount(0);
        sessionService.createSession(session);
        sessionTeamService.createTeamSession(session.getId());
        sessionTeamService.createTeamSession(session.getId());
        return session;
    }
    @DeleteMapping("deleteSession/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable  Long id){
        sessionTeamService.deleteAllTeamSessions(id);
        sessionService.deleteSession(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("joinSessionTeam/{sessionId}/{team}")
    public ResponseEntity<SessionTeamDTO> joinTeamSession(@PathVariable Long sessionId, @PathVariable Team team) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getPlayer().getId();
        SessionTeamDTO teamSession = sessionTeamService.joinTeamSession(sessionId, playerId, team);
        if (teamSession != null) {
            return ResponseEntity.ok(teamSession);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("leaveSessionTeam/{sessionId}/{team}")
    public ResponseEntity<String> leaveTeam(@PathVariable Long sessionId,@PathVariable Team team) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long playerId = userService.getUserByUsername(username).getPlayer().getId();
        sessionTeamService.leaveTeam(playerId, sessionId, team);
        return ResponseEntity.ok("Player removed from the team successfully");
    }
    @GetMapping("/getTeam/{sessionId}")
    public ResponseEntity<List<SessionTeamDTO>> getTeamsBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionTeamService.getTeamsBySession(sessionId));
    }
    @GetMapping("/getPlayers/{sessionId}")
    public ResponseEntity<Map<String, Object>> getPlayersInSession(@PathVariable Long sessionId) {
        List<AllPlayersDTO> players = sessionTeamService.getAllPlayersInSession(sessionId);
        Map<String, Object> response = new HashMap<>();
        response.put("data", players);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getNumberOfPlayers/{sessionId}/{team}")
    public ResponseEntity<Integer> getNumberOfPlayers(
            @PathVariable Long sessionId,
            @PathVariable Team team) {
        int count = sessionService.getNumberOfPlayers(sessionId, team);
        return ResponseEntity.ok(count);
    }

}
