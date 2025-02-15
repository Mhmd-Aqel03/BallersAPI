package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
