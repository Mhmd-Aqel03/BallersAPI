package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.dataTransferObjects.UserDTO;
import com.ballersApi.ballersApi.exceptions.UserCreationErrorException;
import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.UserRepository;
import com.ballersApi.ballersApi.security.AppUserDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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
        }catch (TransactionSystemException e) {
            Throwable rootCause = e.getRootCause(); // Get the actual database error
            throw new UserCreationErrorException("Transaction error: " + (rootCause != null ? rootCause.getMessage() : e.getMessage()));
        }
        catch (DataIntegrityViolationException e){
            throw new UserCreationErrorException("Something went wrong while creating user: " + e.getMostSpecificCause().getMessage());
        }
        catch (Exception e) {
            throw new UserCreationErrorException("Something went wrong while creating user: " + e.getMessage());
        }
    }

    public Optional<User> getUsersByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void checkUserInput(String username, String  password) {
        // Validate username format
        // ChatGPT wrote this regex, hopefully it works lol.
        if (!username.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new UserCreationErrorException("Username must start with a letter and contain only letters, numbers, or underscores.");
        }

        // Check if UserName is already in use
        Optional<User> user = getUsersByUsername(username);

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
