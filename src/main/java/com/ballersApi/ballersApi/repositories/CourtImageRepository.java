package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.CourtImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtImageRepository extends JpaRepository<CourtImage, Long> {
    List<CourtImage> findByCourtId(Long courtId);
}
