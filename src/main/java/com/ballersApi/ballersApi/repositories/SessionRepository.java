package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findById(Long id);
    List<Session> findByMatchDateAfterAndIsDoneFalse(LocalDate now);
    List<Session> findByMatchDateBetweenAndIsDoneFalse(LocalDate startDate, LocalDate endDate);
    void deleteAllByCourt(Court court);
    List<Session> findByRefereeAndIsDoneFalse(User referee);
}
