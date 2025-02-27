package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.ChangePasswordDTO;
import com.ballersApi.ballersApi.dataTransferObjects.LoginDTO;
import com.ballersApi.ballersApi.dataTransferObjects.PlayerDTO;
import com.ballersApi.ballersApi.dataTransferObjects.TokenDTO;
import com.ballersApi.ballersApi.exceptions.*;
import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Role;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.UserRepository;
import com.ballersApi.ballersApi.util.CodeGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final UserService userService;

    private final JwtService jwtService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to persist to the Database: " + e.getMessage());
        }
    }

    public Player getPlayerByUsername(String username) {
        // User Service should throw the not found here
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

            if (player.getRefreshToken() == null || !player.getRefreshToken().equals(token)) {
                throw new JwtTokenValidationException("Refresh Token is either invalid or outDated");
            }

            // Generate new Refresh Token.
            newToken = jwtService.generateRefreshToken(username);
            // Update refresh token.
            updateRefreshToken(username, newToken);

            return newToken;
        } catch (JwtTokenValidationException e) {
            throw new JwtTokenValidationException(e.getMessage());
        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to persist to the Database: " + e.getMessage());
        }
    }

    @Transactional
    public void logout(String username) {
        try {
            Player player = getPlayerByUsername(username);

            player.setRefreshToken(null);

            playerRepository.save(player);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to persist to the Database: " + e.getMessage());
        }

    }

    @Transactional
    public void requestCode(String username) {
        try {
            User user = userService.getUserByUsername(username);
            String code = CodeGenerator.generateCode();

            Player player = getPlayerByUsername(username);

            player.setEmailVerificationCode(code);

            playerRepository.save(player);

            emailService.sendEmail(user.getEmail(), "Code", code);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to persist to the Database: " + e.getMessage());
        }
    }

    @Transactional
    public void verifyCode(String username, String code) {
        try {
            Player player = getPlayerByUsername(username);

            if (player.getEmailVerificationCode() != null && player.getEmailVerificationCode().equals(code)) {
                player.setVerified(true);

                playerRepository.save(player);
            } else {
                throw new CodeVerificationException("Email verification code is invalid");
            }
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (CodeVerificationException e) {
            throw new CodeVerificationException(e.getMessage());
        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to persist to the Database: " + e.getMessage());
        }
    }

    @Transactional
    public TokenDTO login(LoginDTO loginDTO) {
        TokenDTO tokenDTO = new TokenDTO();
        // This throws an exception if no User is found.
        User user = userService.getUserByUsername(loginDTO.getUsername());


        if (user.getRole() == Role.ROLE_PLAYER) {

            if (user.getUsername().equals(loginDTO.getUsername()) && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {

                Player player = getPlayerByUsername(loginDTO.getUsername());

                if (!player.isVerified()) {
                    throw new AuthorizationFailedException("Player's Email is not verified");
                }

                tokenDTO.setAccessToken(jwtService.generateAccessToken(loginDTO.getUsername()));
                tokenDTO.setRefreshToken(jwtService.generateRefreshToken(loginDTO.getUsername()));

                // Update refresh Token for player
                updateRefreshToken(loginDTO.getUsername(), tokenDTO.getRefreshToken());

                return tokenDTO;
            } else {
                throw new AuthenticationFailedException("Username or password are incorrect");
            }
        } else {
            if (user.getUsername().equals(loginDTO.getUsername()) && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                tokenDTO.setAccessToken(jwtService.generateAccessToken(loginDTO.getUsername()));

                return tokenDTO;
            } else {
                throw new AuthenticationFailedException("Username or password are incorrect");
            }
        }
    }

    @Transactional
    public void requestPassCode(String username) {
        try {
            User user = userService.getUserByUsername(username);
            String code = CodeGenerator.generateCode();

            Player player = getPlayerByUsername(username);

            player.setPasswordChangeCode(code);

            playerRepository.save(player);

            emailService.sendEmail(user.getEmail(), "Password Change Code", code);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to persist to the Database: " + e.getMessage());
        }
    }


    public void verifyPassCode(String username, String code) {
        Player player = getPlayerByUsername(username);

        if (!(player.getPasswordChangeCode() != null && player.getPasswordChangeCode().equals(code)))
            throw new CodeVerificationException("Password verification code is invalid");

    }

    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        User user = userService.getUserByUsername(changePasswordDTO.getUsername());

        verifyPassCode(changePasswordDTO.getUsername(), changePasswordDTO.getCode());

        //Check password
        if (!changePasswordDTO.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$")) {
            throw new UserCreationErrorException(
                    "Password must include at least one lowercase letter, one uppercase letter, one number, and one special character."
            );
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

        try {
            userRepository.save(user);
        } catch (DataAccessException e){
            throw new DatabaseConnectionErrorException("Can't Update user's password");
        }
    }
}
