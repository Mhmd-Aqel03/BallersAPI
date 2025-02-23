package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.PlayerDTO;
import com.ballersApi.ballersApi.dataTransferObjects.RefreshTokenDTO;
import com.ballersApi.ballersApi.dataTransferObjects.TokenDTO;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.services.PlayerService;
import com.ballersApi.ballersApi.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final PlayerService playerService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> registerPlayer(@Valid @RequestBody PlayerDTO playerDTO) {

        // Create response object
        Map<String, Object> response = new HashMap<>();

        // Add the player
        playerService.addPlayer(playerDTO);

        // Generate access and refresh JWT Tokens, and put them in the response.
        TokenDTO tokens = new TokenDTO();
        tokens = jwtService.generateToken(playerDTO.getUsername());

        // Update refresh Token for player
        playerService.updateRefreshToken(playerDTO.getUsername(), tokens.getRefreshToken());

        response.put("access Token", tokens.getAccessToken());
        response.put("refresh Token", tokens.getRefreshToken());

        // Return JWT
        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser() {
//
//    }
//    @PostMapping("/refreshToken")
//    public ResponseEntity<String> refreshToken() {
//
//    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        // Create response object
        Map<String, Object> response = new HashMap<>();
        // Get token from POST Body, and username from security context
        String token = refreshTokenDTO.getToken();
        String username = jwtService.extractUsername(token);

        playerService.updateRefreshToken(username, token);

        response.put("token", token);

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

}
