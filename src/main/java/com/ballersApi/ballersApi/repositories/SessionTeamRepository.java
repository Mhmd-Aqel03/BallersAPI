package com.ballersApi.ballersApi.repositories;

import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SessionTeamRepository extends JpaRepository<SessionTeam, Long> {
    List<SessionTeam> findBySession(Session session);
    List<SessionTeam> findBySessionId(Long sessionId);
    List<SessionTeam> id(Long id);
}
