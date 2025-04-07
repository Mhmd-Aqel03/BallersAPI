package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.JsonWebTokens.JwtService;
import com.ballersApi.ballersApi.dataTransferObjects.*;
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
public class PlayerAuthService {

    private final PlayerRepository playerRepository;

    private final UserService userService;

    private final JwtService jwtService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;
    @Transactional
    public void addPlayer(PlayerDTO playerDTO) {
        User newUser = new User();

        userService.checkUserInput(playerDTO.getUsername(), playerDTO.getPassword(),playerDTO.getEmail());

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
        try {
            userService.addUser(newUser);
        } catch (DataAccessException ex) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to add user: " + ex.getMessage());
        }
    }

    @Transactional
    public void updateRefreshToken(Long id, String refreshToken) {

        Player player = getPlayerById(id);

        player.setRefreshToken(refreshToken);
        try {
            playerRepository.save(player);
        } catch (DataAccessException ex) {
            throw new DatabaseConnectionErrorException("Something went wrong while trying to update refresh token: " + ex.getMessage());
        }

    }

    public Player getPlayerByUsername(String username) {
        // User Service should throw a Not found
        User user = userService.getUserByUsername(username);

        return user.getPlayer();
    }

    public Player getPlayerByEmail(String email) {
        User user = userService.getUserByEmail(email);

        return user.getPlayer();
    }

    public Player getPlayerById(Long id) {
        User user = userService.getUserById(id);

        return user.getPlayer();
    }

    @Transactional
    public TokenDTO refreshToken(String token) {
        TokenDTO tokenDto = new TokenDTO();
        Long id = Long.parseLong(jwtService.extractUsername(token));

        // Validate token, should throw exceptions for invalid tokens.
        jwtService.validateToken(token);

        Player player = getPlayerById(id);

        if (player.getRefreshToken() == null || !player.getRefreshToken().equals(token)) {
            throw new JwtTokenValidationException("Refresh Token is either invalid or outDated");
        }

        // Generate new Refresh Token.
        tokenDto.setRefreshToken(jwtService.generateRefreshToken(id));

        // Generate new Access token
        tokenDto.setAccessToken(jwtService.generateAccessToken(id));

        // Update refresh token.
        updateRefreshToken(id, tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Transactional
    public void logout(String username) {

        Player player = getPlayerByUsername(username);

        player.setRefreshToken(null);

        try {
            playerRepository.save(player);
        } catch (DataAccessException ex) {
            throw new DatabaseConnectionErrorException("soemthing went wrong while trying to logout user: " + ex.getMessage());
        }

    }

    @Transactional
    public void requestCode(String username) {

        User user = userService.getUserByUsername(username);
        String code = CodeGenerator.generateCode();

        Player player = getPlayerByUsername(username);

        player.setEmailVerificationCode(code);

        try {
            playerRepository.save(player);
        } catch (DataAccessException ex) {
            throw new DatabaseConnectionErrorException("something went wrong while trying to update player verification code: " + ex.getMessage());
        }
        emailService.sendEmail(user.getEmail(), "Code", code);

    }

    @Transactional
    public void verifyCode(String username, String code) {

        Player player = getPlayerByUsername(username);

        if (player.getEmailVerificationCode() != null && player.getEmailVerificationCode().equals(code)) {
            player.setVerified(true);

            try {
                playerRepository.save(player);
            } catch (DataAccessException ex) {
                throw new DatabaseConnectionErrorException("something went wrong while trying to update player verified status: " + ex.getMessage());
            }
        } else {
            throw new CodeVerificationException("Email verification code is invalid");

        }
    }

    @Transactional
    public TokenDTO login(LoginDTO loginDTO) {
        TokenDTO tokenDTO = new TokenDTO();
        // This throws an exception if no User is found.
        User user = userService.getUserByEmail(loginDTO.getEmail());

        if (user.getRole() == Role.ROLE_PLAYER) {

            if (user.getEmail().equals(loginDTO.getEmail()) && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {

                Player player = getPlayerByEmail(loginDTO.getEmail());

                if (!player.isVerified()) {
                    throw new AuthorizationFailedException("Player's Email is not verified");
                }

                tokenDTO.setAccessToken(jwtService.generateAccessToken(user.getId()));
                tokenDTO.setRefreshToken(jwtService.generateRefreshToken(user.getId()));

                // Update refresh Token for player
                updateRefreshToken(user.getId(), tokenDTO.getRefreshToken());

                return tokenDTO;
            } else {
                throw new AuthenticationFailedException("Email or password are incorrect");
            }
        } else {
            if (user.getEmail().equals(loginDTO.getEmail()) && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                tokenDTO.setAccessToken(jwtService.generateAccessToken(user.getId()));

                return tokenDTO;
            } else {
                throw new AuthenticationFailedException("Email or password are incorrect");
            }
        }
    }

    @Transactional
    public void requestPassCode(String email) {

        User user = userService.getUserByEmail(email);
        String code = CodeGenerator.generateCode();

        Player player = getPlayerByUsername(user.getUsername());

        player.setPasswordChangeCode(code);
        try {
            playerRepository.save(player);
        } catch (DataAccessException ex) {
            throw new DatabaseConnectionErrorException("something went wrong while trying to update player's pass Code: " + ex.getMessage());
        }
        emailService.sendEmail(user.getEmail(), "Password Change Code", code);

    }


    public void verifyPassCode(String email, String code) {
        Player player = getPlayerByEmail(email);

        if (!(player.getPasswordChangeCode() != null && player.getPasswordChangeCode().equals(code)))
            throw new CodeVerificationException("Password verification code is invalid");
    }

    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        User user = userService.getUserByEmail(changePasswordDTO.getEmail());

        verifyPassCode(changePasswordDTO.getEmail(), changePasswordDTO.getCode());

        //Check password
        if (!changePasswordDTO.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$")) {
            throw new UserCreationErrorException(
                    "Password must include at least one lowercase letter, one uppercase letter, one number, and one special character."
            );
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

        userService.updateUser(user);
    }

    public void updatePlayer(String username, UpdatePlayerDTO updatePlayerDTO) {
        Player player = getPlayerByUsername(username);
        User user = userService.getUserByUsername(username);

        if (userService.checkIfUserExists(updatePlayerDTO.getUsername())) {
            throw new UsernameAlreadyTakenException("Username " + updatePlayerDTO.getUsername() + " is already taken");
        }

        user.setUsername(updatePlayerDTO.getUsername());

        player.setPostion(updatePlayerDTO.getPosition());
        player.setPhotoUrl(updatePlayerDTO.getPhotoUrl());

        userService.updateUser(user);
    }
}
