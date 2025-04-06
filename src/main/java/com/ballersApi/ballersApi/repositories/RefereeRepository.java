package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Referee;
import com.ballersApi.ballersApi.models.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefereeRepository extends JpaRepository<Referee, Long> {
    Optional<Referee> findById(Long id);
}
