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

    private void checkUserInput(PlayerDTO playerDTO){
        // Check if UserName is already in use
        UserDetails user =  userService.loadUserByUsername(playerDTO.getUsername());

        if(user != null){
            throw new UserCreationErrorException("User Name already exists");
        }


    }
}
