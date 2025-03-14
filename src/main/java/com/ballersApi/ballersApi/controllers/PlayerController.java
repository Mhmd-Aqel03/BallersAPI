package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.dataTransferObjects.PlayerIdDTO;
import com.ballersApi.ballersApi.dataTransferObjects.UsernameDTO;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.services.PlayerAuthService;
import com.ballersApi.ballersApi.services.PlayerService;
import com.ballersApi.ballersApi.services.UserService;
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

    private final PlayerService playerService;

    private final UserService userService;

    @GetMapping("/getUser")
    public ResponseEntity<Map<String, Object>> getPlayer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> response = new HashMap<>();

        User user = userService.getUserByUsername(username);

        response.put("user", user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<Map<String, Object>> getPlayer(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.getUserById(id);

        response.put("user", user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchUsers/{username}")
    public ResponseEntity<Map<String, Object>> searchUsers(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        ArrayList <User> users = userService.searchUsers(username);
        if(!users.isEmpty()){
            response.put("users", users);

            return ResponseEntity.ok(response);
        } else{
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/endorseGoodLeader")
    public ResponseEntity<Map<String,Object>> endorseGoodLeader(@Valid @RequestBody PlayerIdDTO playerIdDTO) {
        Map<String, Object> response = new HashMap<>();

        playerService.endorseGoodLeader(playerIdDTO.getPlayerId());

        response.put("msg", "Player with id: " + playerIdDTO.getPlayerId() + " has been endorsed successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/endorseTeamPlayer")
    public ResponseEntity<Map<String,Object>> endorseTeamPlayer(@Valid @RequestBody PlayerIdDTO playerIdDTO) {
        Map<String, Object> response = new HashMap<>();

        playerService.endorseTeamPlayer(playerIdDTO.getPlayerId());

        response.put("msg", "Player with id: " + playerIdDTO.getPlayerId() + " has been endorsed successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/endorsePositiveAttitude")
    public ResponseEntity<Map<String,Object>> endorsePositiveAttitude(@Valid @RequestBody PlayerIdDTO playerIdDTO) {
        Map<String, Object> response = new HashMap<>();

        playerService.endorsePositiveAttitude(playerIdDTO.getPlayerId());

        response.put("msg", "Player with id: " + playerIdDTO.getPlayerId() + " has been endorsed successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/addFavourite")
    public ResponseEntity<Map<String, Object>> addFav(@Valid @RequestBody PlayerIdDTO playerIdDTO) {
        Map<String, Object> response = new HashMap<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        playerService.addFavourite(username, playerIdDTO.getPlayerId());

        response.put("msg", "Player with id: " + playerIdDTO.getPlayerId() + " has been added to the user's favourite list successfully");

        return ResponseEntity.ok(response);
    }
}
