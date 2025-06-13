package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class TeamInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private InviteStatus status;
    private Team teamName;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Player receiver;

    @ManyToOne
    @JoinColumn(name = "sessionteam_id")
    private SessionTeam team;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

}
