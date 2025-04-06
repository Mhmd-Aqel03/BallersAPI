package com.ballersApi.ballersApi.controllers;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.*;
import com.ballersApi.ballersApi.services.EmailService;
import com.ballersApi.ballersApi.services.PlayerAuthService;
import com.ballersApi.ballersApi.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final PlayerAuthService playerAuthService;

    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        // Create response object
        Map<String, Object> response = new HashMap<>();

        // Add the player
        playerAuthService.addPlayer(playerDTO);
        // Send Email Code
        playerAuthService.requestCode(playerDTO.getUsername());

        response.put("message", "Player registered Successfully");

        // Return JWT
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        // The Jwt token that will be returned(Inshallah).
        TokenDTO tokenDTO = playerAuthService.login(loginDTO);
        Map<String, Object> response = new HashMap<>();

        response.put("accessToken", tokenDTO.getAccessToken());

        if (tokenDTO.getRefreshToken() != null)
            response.put("refreshToken", tokenDTO.getRefreshToken());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // This is only for Players, Since you can't invalidate Access tokens (without persistence).
    // You need to be already logged in (i.e have an access token in your auth header).
    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('ROLE_PLAYER')")
    public ResponseEntity<Map<String, Object>> login() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> response = new HashMap<>();

        playerAuthService.logout(username);

        response.put("message", "User logged Out Successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // This is only for players, Admins and Referees will only get a 1 day access token and will need to login again for security(I ain't doing all dat)
    // Why isn't this in the /players controller? shut up nerd.
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        TokenDTO tokenDto;
        Map<String, Object> response = new HashMap<>();

        // Get token from POST Body.
        String token = refreshTokenDTO.getToken();

        tokenDto = playerAuthService.refreshToken(token);

        response.put("Access token", tokenDto.getAccessToken());
        response.put("Refresh token", tokenDto.getRefreshToken());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/secret")
    @PreAuthorize("hasAuthority('ROLE_PLAYER')")
    public ResponseEntity<Map<String, Object>> getSecret() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> response = new HashMap<>();

        response.put("msg", username);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/requestCode")
    public ResponseEntity<Map<String, Object>> requestCode(@Valid @RequestBody UsernameDTO usernameDTO) {
        Map<String, Object> response = new HashMap<>();

        playerAuthService.requestCode(usernameDTO.getUsername());

        response.put("message", "Request Code sent to User's email");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<Map<String, Object>> verifyCode(@Valid @RequestBody VerifyCodeDTO verifyCodeDTO) {
        Map<String, Object> response = new HashMap<>();

        playerAuthService.verifyCode(verifyCodeDTO.getUsername(), verifyCodeDTO.getCode());

        response.put("message", "Player email verified Successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/requestPassCode")
    public ResponseEntity<Map<String, Object>> requestPassCode(@Valid @RequestBody EmailDTO emailDTO) {
        String email = emailDTO.getEmail();
        Map<String, Object> response = new HashMap<>();

        playerAuthService.requestPassCode(email);

        response.put("message", "Password Change Code sent to User's email");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Map<String,Object>> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO){
        Map<String, Object> response = new HashMap<>();

        playerAuthService.changePassword(changePasswordDTO);

        response.put("message", "Player's password changed successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
