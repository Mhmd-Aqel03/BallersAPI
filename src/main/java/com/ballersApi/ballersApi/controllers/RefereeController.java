package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.Team;
import com.ballersApi.ballersApi.services.RefereeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Referee")
public class RefereeController {
    @Autowired
    private  RefereeService refereeService;

    @PostMapping("/finalize/{id}/{winner}")
    public ResponseEntity<String> finalizeSession(@PathVariable Long id, @PathVariable Team winner) {
        refereeService.finalizeSessionResult(id, winner);
        return ResponseEntity.ok("Session " + id + " finalized. Winning team: " + winner);
    }
    @GetMapping("/sessions/{refereeId}")
    public ResponseEntity<Map<String, Object>> getSessionsByReferee(@PathVariable Long refereeId) {
        Map<String, List<SessionDTO>> groupedSessions = refereeService.getSessionsByRefereeId(refereeId);

        Map<String, Object> response = new HashMap<>();
        response.put("data", groupedSessions);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/mvp/{sessionId}/{playerId}")
    public ResponseEntity<String> chooseMvp(
            @PathVariable Long sessionId,
            @PathVariable Long playerId
    ) {
        refereeService.chooseMvp(sessionId, playerId);
        return ResponseEntity.ok("MVP has been selected successfully.");
    }

}
