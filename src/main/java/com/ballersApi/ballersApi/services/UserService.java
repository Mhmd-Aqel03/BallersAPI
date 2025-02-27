package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.UserDTO;
import com.ballersApi.ballersApi.exceptions.DatabaseConnectionErrorException;
import com.ballersApi.ballersApi.exceptions.UserCreationErrorException;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public void addUser(UserDTO userDto) {
        try {
            User newUser = new User();
            newUser.setUsername(userDto.getUsername());
            newUser.setPassword(userDto.getPassword());
            newUser.setEmail(userDto.getEmail());

            userRepository.save(newUser);
        } catch (Exception e) {
            throw new UserCreationErrorException("Something went wrong while creating the user: " + e.getMessage());
        }
    }

    @Transactional
    public void addUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (TransactionSystemException e) {
            Throwable rootCause = e.getRootCause(); // Get the actual database error
            throw new UserCreationErrorException("Transaction error: " + (rootCause != null ? rootCause.getMessage() : e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            throw new UserCreationErrorException("Something went wrong while creating user: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new UserCreationErrorException("Something went wrong while creating user: " + e.getMessage());
        }
    }

    public User getUserByUsername(String username) {
        Optional<User> user;
        try {
            user = userRepository.findByUsername(username);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Something went wrong while trying to access user" + e.getMessage());
        }


        // orElseThrow will return the object if it exists, or throw and exception if it doesn't. So cool shout out to Java.
        return user.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));


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
            throw new UserCreationErrorException("Username already Taken");
        }

        //Check password
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$")) {
            throw new UserCreationErrorException(
                    "Password must include at least one lowercase letter, one uppercase letter, one number, and one special character."
            );
        }

    }

}
