package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.dataTransferObjects.UsernameDTO;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.services.PlayerAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerAuthService playerAuthService;

    @GetMapping("/getPlayer")
    public ResponseEntity<Map<String, Object>> getPlayer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> response = new HashMap<>();

        Player player = playerAuthService.getPlayerByUsername(username);

        response.put("player", player);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getPlayer/{id}")
    public ResponseEntity<Map<String, Object>> getPlayer(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        Player player = playerAuthService.getPlayerById(id);

        response.put("player", player);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchPlayer/{username}")
    public ResponseEntity<Map<String, Object>> searchPlayer(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        ArrayList <Player> players = ;
    }
}
