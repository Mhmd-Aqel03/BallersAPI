package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByMatchDateAfter(LocalDate now);
    List<Session> findByMatchDateBetween(LocalDate startDate, LocalDate endDate);
    List<Session> findByReferee(User referee);
    Optional<Session> findById(Long id);
}
