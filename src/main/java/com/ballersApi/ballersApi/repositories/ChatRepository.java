package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findBySessionId(Long sessionId);
}
