package com.ballersApi.ballersApi.repositries;

import com.ballersApi.ballersApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositry extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
