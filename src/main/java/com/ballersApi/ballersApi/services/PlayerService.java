package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.PlayerDTO;
import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
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
}
