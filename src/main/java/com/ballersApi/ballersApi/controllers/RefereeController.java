package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.dataTransferObjects.SessionDTO;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Team;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.services.RefereeService;
import com.ballersApi.ballersApi.services.UserService;
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
    private RefereeService refereeService;
    @Autowired
    private  UserService userService;


    @PostMapping("/finalize/{id}/{winner}/{playerId}")
    public ResponseEntity<String> finalizeSession(@PathVariable Long id, @PathVariable Team winner,@PathVariable Long playerId) {
        refereeService.finalizeSessionResult(id, winner);
        refereeService.chooseMvp(id,playerId);
        User user = userService.getUserByPlayerId(playerId);
        return ResponseEntity.ok("Session " + id + " finalized. Winning team: " + winner+" mvp :" + user.getUsername());
    }

    @GetMapping("/sessions/{refereeId}")
    public ResponseEntity<Map<String, Object>> getSessionsByReferee(@PathVariable Long refereeId) {
        Map<String, List<SessionDTO>> groupedSessions = refereeService.getSessionsByRefereeId(refereeId);

        Map<String, Object> response = new HashMap<>();
        response.put("data", groupedSessions);

        return ResponseEntity.ok(response);
    }

//    @PostMapping("/mvp/{sessionId}/{playerId}")
//    public ResponseEntity<String> chooseMvp(
//            @PathVariable Long sessionId,
//            @PathVariable Long playerId
//    ) {
//        refereeService.chooseMvp(sessionId, playerId);
//        return ResponseEntity.ok("MVP has been selected successfully.");
//    }

}
