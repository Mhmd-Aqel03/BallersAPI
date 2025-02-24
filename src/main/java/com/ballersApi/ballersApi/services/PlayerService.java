package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.PlayerDTO;
import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.exceptions.JwtTokenValidationException;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Role;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final UserService userService;

    private final JwtService jwtService;

    @Transactional
    public void addPlayer(PlayerDTO playerDTO) {
        User newUser = new User();

        userService.checkUserInput(playerDTO.getUsername(), playerDTO.getPassword());

        // Create User
        newUser.setUsername(playerDTO.getUsername());
        newUser.setPassword(playerDTO.getPassword());
        newUser.setEmail(playerDTO.getEmail());
        newUser.setRole(Role.ROLE_PLAYER);

        // Create Player
        Player newPlayer = new Player();
        newPlayer.setPostion(playerDTO.getPosition());
        newPlayer.setPhoneNumber(playerDTO.getPhoneNumber());
        newPlayer.setPhotoUrl(playerDTO.getPhotoUrl());

        // Link User to player
        newUser.setPlayer(newPlayer);

        // Save User
        userService.addUser(newUser);
    }

    @Transactional
    public void updateRefreshToken(String username, String refreshToken) {
        try {
            Player player = getPlayerByUsername(username);

            player.setRefreshToken(refreshToken);

            playerRepository.save(player);

        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to persist to the Database: " + e.getMessage());
        }
    }

    public Player getPlayerByUsername(String username) {
        User user = userService.getUserByUsername(username);

        return user.getPlayer();
    }

    @Transactional
    public String refreshToken(String token) {
        try {
            String newToken;
            String username = jwtService.extractUsername(token);

            // Validate token, should throw exceptions for invalid tokens.
            jwtService.validateToken(token);

            Player player = getPlayerByUsername(username);

            if(player.getRefreshToken() ==  null || !player.getRefreshToken().equals(token)){
                throw new JwtTokenValidationException("Refresh Token is either invalid or outDated");
            }

            // Generate new Refresh Token.
            newToken = jwtService.generateRefreshToken(username);
            // Update refresh token.
            updateRefreshToken(username, newToken);

            return newToken;
        }catch (JwtTokenValidationException e){
            throw new JwtTokenValidationException(e.getMessage());
        }
        catch (Exception e){
            throw new DatabaseConnectionErrorException("Something went wrong while trying to persist to the Database: " + e.getMessage());
        }
    }
}
