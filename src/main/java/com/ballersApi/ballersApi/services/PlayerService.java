package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.PlayerDTO;
import com.ballersApi.ballersApi.dataTransferObjects.UserDTO;
import com.ballersApi.ballersApi.exceptions.UserCreationErrorException;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Role;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserService userService;

    public void addPlayer(PlayerDTO playerDTO){
        User newUser = new User();

        checkUserInput(playerDTO);

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

        // Save Player
        playerRepository.save(newPlayer);

        // Save User
        userService.addUser(newUser);
    }

    private void checkUserInput(PlayerDTO playerDTO) {
        // Validate username format
        String username = playerDTO.getUsername();
        // ChatGPT wrote this regex, hopefully it works.
        if (!username.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new UserCreationErrorException("Username must start with a letter and contain only letters, numbers, or underscores.");
        }

        // Check if UserName is already in use
        Optional<User> user = userService.getUsersByUsername(username);

        // Basically "!= null"
        if (user.isPresent()) {
            throw new UserCreationErrorException("Username already Taken");
        }
    }
}
