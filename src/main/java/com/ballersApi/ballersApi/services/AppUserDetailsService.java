package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.UserDTO;
import com.ballersApi.ballersApi.exceptions.UserCreationErrorException;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.UserRepository;
import com.ballersApi.ballersApi.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<User> userDetail = userRepository.findByUsername(username);

        return userDetail.map(AppUserDetails::new)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found"));
    }

    public void addUser(UserDTO userDto) {
        try {
            User newUser = new User();
            newUser.setUsername(userDto.getUsername());
            newUser.setPassword(userDto.getPassword());
            newUser.setEmail(userDto.getEmail());

            userRepository.save(newUser);
        } catch (Exception e) {
            throw new UserCreationErrorException("Something went wrong while creating user: " + e.getMessage());
        }
    }

}
