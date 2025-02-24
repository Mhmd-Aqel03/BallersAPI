package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.LoginDTO;
import com.ballersApi.ballersApi.dataTransferObjects.PlayerDTO;
import com.ballersApi.ballersApi.dataTransferObjects.RefreshTokenDTO;
import com.ballersApi.ballersApi.dataTransferObjects.TokenDTO;
import com.ballersApi.ballersApi.exceptions.JwtTokenValidationException;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.services.PlayerService;
import com.ballersApi.ballersApi.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final PlayerService playerService;


    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> registerPlayer(@Valid @RequestBody PlayerDTO playerDTO) {

        // Create response object
        Map<String, Object> response = new HashMap<>();

        // Add the player
        playerService.addPlayer(playerDTO);

        // Generate access and refresh JWT Tokens, and put them in the response.
        TokenDTO tokens = new TokenDTO();
        tokens.setAccessToken(jwtService.generateAccessToken(playerDTO.getUsername()));
        tokens.setRefreshToken(jwtService.generateRefreshToken(playerDTO.getUsername()));

        // Update refresh Token for player
        playerService.updateRefreshToken(playerDTO.getUsername(), tokens.getRefreshToken());

        response.put("accessToken", tokens.getAccessToken());
        response.put("refreshToken", tokens.getRefreshToken());

        // Return JWT
        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        // The Jwt token that will be returned(Inshallah)
        String token = userService.login(loginDTO);
        Map<String, Object> response = new HashMap<>();

        response.put("accessToken", token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // This is only for players, Admins and Referees will only get a 7 day access token and will need to login again for security(I ain't doing all dat)
    // Why isn't this in the /players controller? shut up nerd.
    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        String newToken;
        Map<String, Object> response = new HashMap<>();

        // Get token from POST Body.
        String token = refreshTokenDTO.getToken();

        newToken = playerService.refreshToken(token);

        response.put("token", newToken);

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/secret")
    @PreAuthorize("hasRole('ROLE_PLAYER')")
    public ResponseEntity<Map<String,Object>> getSecret() {
        Map<String, Object> response = new HashMap<>();

        response.put("msg","secret");

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }
}
