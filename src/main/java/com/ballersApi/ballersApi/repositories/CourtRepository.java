package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

}
