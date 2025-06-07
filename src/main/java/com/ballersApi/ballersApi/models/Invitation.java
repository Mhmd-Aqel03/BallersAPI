package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private boolean status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Player receiver;
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

}
