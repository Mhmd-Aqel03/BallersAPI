package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.*;
import com.ballersApi.ballersApi.exceptions.JwtTokenValidationException;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.services.EmailService;
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

    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> registerPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        // Create response object
        Map<String, Object> response = new HashMap<>();

        // Add the player
        playerService.addPlayer(playerDTO);

        response.put("message", "Player registered Successfully");

        // Return JWT
        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        // The Jwt token that will be returned(Inshallah).
        TokenDTO tokenDTO = playerService.login(loginDTO);
        Map<String, Object> response = new HashMap<>();

        // Generate access and refresh JWT Tokens, and put them in the response.
        TokenDTO tokens = new TokenDTO();
        tokens.setAccessToken(jwtService.generateAccessToken(loginDTO.getUsername()));
        tokens.setRefreshToken(jwtService.generateRefreshToken(loginDTO.getUsername()));

        response.put("accessToken", tokens.getAccessToken());

        if(tokenDTO.getRefreshToken() != null)
            response.put("refreshToken", tokens.getRefreshToken());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // This is only for Players, Since you can't invalidate Access tokens (without persistence).
    // You need to be already logged in (i.e have an access token in your auth header).
    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('ROLE_PLAYER')")
    public ResponseEntity<Map<String,Object>> login() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> response = new HashMap<>();

        playerService.logout(username);

        response.put("message", "User logged Out Successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // This is only for players, Admins and Referees will only get a 1 day access token and will need to login again for security(I ain't doing all dat)
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
    @PreAuthorize("hasAuthority('ROLE_PLAYER')")
    public ResponseEntity<Map<String,Object>> getSecret() {
        Map<String, Object> response = new HashMap<>();

        response.put("msg","secret");

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/requestCode")
    public ResponseEntity<Map<String,Object>> requestCode(@Valid @RequestBody UsernameDTO usernameDTO) {
        Map<String, Object> response = new HashMap<>();

        playerService.requestCode(usernameDTO.getUsername());

        response.put("message", "Request Code sent to User's email");

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<Map<String,Object>> verifyCode(@Valid @RequestBody VerifyCodeDTO verifyCodeDTO){
        Map<String, Object> response = new HashMap<>();

        playerService.verifyCode(verifyCodeDTO.getUsername(), verifyCodeDTO.getCode());

        response.put("message", "Player email verified Successfully");

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }
}
