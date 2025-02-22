package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.PlayerDTO;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.services.PlayerService;
import com.ballersApi.ballersApi.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final PlayerService playerService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> registerPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        // Add the player
        playerService.addPlayer(playerDTO);

        // Generate JWT Token
        String token = jwtService.generateToken(playerDTO.getUsername());

        // Return JWT
        return ResponseEntity.ok(token);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser() {
//
//    }
//    @PostMapping("/refreshToken")
//    public ResponseEntity<String> refreshToken() {
//
//    }

}
