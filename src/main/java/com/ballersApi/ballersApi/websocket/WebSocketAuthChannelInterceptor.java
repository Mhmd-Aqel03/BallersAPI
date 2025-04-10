package com.ballersApi.ballersApi.websocket;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.exceptions.JwtTokenValidationException;
import com.ballersApi.ballersApi.security.AppUserDetails;
import com.ballersApi.ballersApi.security.WebSocketUserPrincipal;
import com.ballersApi.ballersApi.services.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final AppUserDetailsService appUserDetailsService;

    @Autowired
    public WebSocketAuthChannelInterceptor(JwtService jwtService, AppUserDetailsService appUserDetailsService) {
        this.jwtService = jwtService;
        this.appUserDetailsService = appUserDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            try {
                List<String> authHeaders = accessor.getNativeHeader("Authorization");
                if (authHeaders == null || authHeaders.isEmpty()) {
                    throw new JwtTokenValidationException("Missing Authorization header in WebSocket request.");
                }

                String bearerToken = authHeaders.get(0);
                if (!bearerToken.startsWith("Bearer ")) {
                    throw new JwtTokenValidationException("Invalid token format.");
                }

                String token = bearerToken.substring(7);
                String username = jwtService.extractUsername(token);
                jwtService.validateToken(token);

                AppUserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
                accessor.setUser(new WebSocketUserPrincipal(userDetails.getUsername())); // Attach user to session

            } catch (Exception e) {
                System.err.println("WebSocket authentication failed: " + e.getMessage());
                return null; // Reject connection instead of throwing an exception
            }
        }
        return message;
    }
}
