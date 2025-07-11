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

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @ManyToMany
    @JoinTable(
            name = "receiver_invitation",
            joinColumns = {
                    @JoinColumn(name = "invitation_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "receiver_id")
            }
    )
    private List<Player> receivers;

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
