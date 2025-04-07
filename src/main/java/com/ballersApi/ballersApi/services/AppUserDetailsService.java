package com.ballersApi.ballersApi.services;

import com.ballersApi.ballersApi.exceptions.UserNotFoundException;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.UserRepository;
import com.ballersApi.ballersApi.security.AppUserDetails;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@NoArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {


    private UserRepository userRepository;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<User> userDetail = userRepository.findByUsername(username);

        return userDetail.map(AppUserDetails::new)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found"));
    }

    public  AppUserDetails loadUserById(Long id) throws UserNotFoundException {
        Optional<User> userDetail = userRepository.findById(id);

        return userDetail.map(AppUserDetails::new)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
    }
}
