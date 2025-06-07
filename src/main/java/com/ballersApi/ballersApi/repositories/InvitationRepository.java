package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Invitation;
import com.ballersApi.ballersApi.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByReceiverId(Long receiverId);
}
