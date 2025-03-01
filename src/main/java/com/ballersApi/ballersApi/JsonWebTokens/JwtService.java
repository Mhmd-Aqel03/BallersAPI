package com.ballersApi.ballersApi.JsonWebTokens;

import com.ballersApi.ballersApi.exceptions.JwtTokenValidationException;
import com.ballersApi.ballersApi.services.AppUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final AppUserDetailsService appUserDetailsService;
    private String secret;
    private final Long accessExpiration = 1000L * 60 * 60 * 24 * 7; // 7 Days
    private final Long refreshExpiration = 1000L * 60 * 60 * 24 * 180; // 180 Days (6 months)

    // This a setter injection
    @Value("${JWT.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    // Generate access token with given username
    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);

        claims.put("role", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null)); // So this is never supposed to be null because the UserService should throw a not found exception.(HopeFully)

        claims.put("type", "access");

        return createToken(claims, username, accessExpiration);
    }

    // Generate refresh token with given username
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("type", "refresh");

        return createToken(claims, username, refreshExpiration);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // extract type
    public String extractType(String token) {return  extractClaim(token,claims -> claims.get("type", String.class));}

    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public void validateToken(String token) {
        // Extract claims, This validates that the JWT isn't incorrect or forged.
        final String username = extractUsername(token);


        if(isTokenExpired(token)){
            throw new JwtTokenValidationException("Token is Expired");
        }
    }

    // Create a JWT token with specified claims and subject (username)
    private String createToken(Map<String, Object> claims, String userName, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Token valid for 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get the signing key for JWT token
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtTokenValidationException("Token is expired: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new JwtTokenValidationException("Token is malformed: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtTokenValidationException("Invalid token signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtTokenValidationException("Something went wrong with token validation, may be an invalid token: " + e.getMessage());
        }
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
