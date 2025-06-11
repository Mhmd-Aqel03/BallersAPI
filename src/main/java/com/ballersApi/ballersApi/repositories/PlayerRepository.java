package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
    Optional<Player> findById(Long id);


}

