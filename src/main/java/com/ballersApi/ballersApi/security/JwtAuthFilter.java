package com.ballersApi.ballersApi.security;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.exceptions.AuthenticationFailedException;
import com.ballersApi.ballersApi.exceptions.AuthorizationFailedException;
import com.ballersApi.ballersApi.exceptions.InvalidTokenException;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.services.AppUserDetailsService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserDetailsService appUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Retrieve the Authorization header
            String authHeader = request.getHeader("Authorization");
            
            String token = null;
            String username = null;
            String type = null;

            // Check if the header starts with "Bearer "
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // Extract token
                try {
                    // Extract username from token
                    username = jwtService.extractUsername(token);
                    type = jwtService.extractType(token);
                } catch (Exception e) {
                    System.out.println("JWT token is invalid : " + e.getMessage());
                    throw new InvalidTokenException("JWT token is invalid : " + e.getMessage());
                }
            }

            // Check type of token
            if(type != null && !type.equals("access")){
                System.out.println("Refresh token can't be used as access token");
                throw new InvalidTokenException("Refresh token can't be used as access token");
            }

            // If the token is valid and no authentication is set in the context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    AppUserDetails appUserDetails = appUserDetailsService.loadUserByUsername(username);

                    // Validate token and set authentication
                    if (jwtService.validateToken(token, appUserDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                appUserDetails,
                                null,
                                appUserDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (Exception e) {
                    System.out.println("User not found: " + e.getMessage());
                    throw new InvalidTokenException("User not found: " + e.getMessage());
                }
            }
            // Continue the filter chain
            filterChain.doFilter(request, response);
        }
        catch (InvalidTokenException e) {
            throw new AuthorizationFailedException(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Something went wrong with Authorization: " + e.getMessage());
            throw new AuthorizationFailedException("Something went wrong with Authorization: " + e.getMessage());
        }
    }
}
