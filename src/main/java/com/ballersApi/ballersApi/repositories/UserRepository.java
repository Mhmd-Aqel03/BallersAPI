package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    ArrayList<User> findTop10ByUsernameContainingIgnoreCase(String username);
}
