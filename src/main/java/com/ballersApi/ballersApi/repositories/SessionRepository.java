package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByMatchDateTimeAfter(LocalDateTime now);

    Optional<Session> findById(Long id);
}
