package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.UserDTO;
import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.exceptions.UserCreationErrorException;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.exceptions.UsernameAlreadyTakenException;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void addUser(UserDTO userDto) {

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(userDto.getPassword());
        newUser.setEmail(userDto.getEmail());
        try {
            userRepository.save(newUser);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Could not save user to database: " + e.getMessage());
        }
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Could not save user to database:  " + e.getMessage());
        }
    }

    public boolean checkIfUserExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User getUserByUsername(String username) {
        Optional<User> user;
        try {
            user = userRepository.findByUsername(username);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to retrieve user" + e.getMessage());
        }

        // orElseThrow will return the object if it exists, or throw and exception if it doesn't. So cool shout out to Java.
        return user.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    public User getUserByEmail(String email) {
        Optional<User> user;
        try {
            user = userRepository.findByEmail(email);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to retrieve user" + e.getMessage());
        }

        // orElseThrow will return the object if it exists, or throw and exception if it doesn't. So cool shout out to Java.
        return user.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public User getUserById(Long id){
        Optional<User> user;
        try{
            user = userRepository.findById(id);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to retrieve user" + e.getMessage());
        }

        // orElseThrow will return the object if it exists, or throw and exception if it doesn't. So cool shout out to Java.
        return user.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public ArrayList<User> searchUsers(String username) {
        Optional<ArrayList<User>> users;
        try{
            users = userRepository.findTop10ByUsernameContainingIgnoreCase(username);
        }catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to retrieve user" + e.getMessage());
        }

        // orElseThrow will return the object if it exists, or throw and exception if it doesn't. So cool shout out to Java.
        return users.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    public void updateUser(User user) {
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to access user: " + e.getMessage());
        }
    }

    public void checkUserInput(String username, String password) {
        // Validate username format
        // ChatGPT wrote this regex, hopefully it works lol.
        if (!username.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new UserCreationErrorException("Username must start with a letter and contain only letters, numbers, or underscores.");
        }

        // Check if UserName is already in use
        Optional<User> user = userRepository.findByUsername(username);

        // Basically "!= null"
        if (user.isPresent()) {
            throw new UsernameAlreadyTakenException("Username "  + username + " is already taken.");
        }

        //Check password
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$")) {
            throw new UserCreationErrorException(
                    "Password must include at least one lowercase letter, one uppercase letter, one number, and one special character."
            );
        }

    }

}
