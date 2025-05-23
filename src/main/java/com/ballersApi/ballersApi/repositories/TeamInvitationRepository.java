package com.ballersApi.ballersApi.repositories;

import ch.qos.logback.core.status.Status;
import com.ballersApi.ballersApi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {

    Optional<TeamInvitation> findBySessionAndReceiver(Session session, Player receiver);
    List<TeamInvitation> findByTeamAndStatus(SessionTeam team, InviteStatus status);



}
