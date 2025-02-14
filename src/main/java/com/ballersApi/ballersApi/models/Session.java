package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;

    // Time stamp for the date and time
    private LocalDateTime matchDateTime;

    private int maxPlayers;

    private int playerCount;

    @ManyToOne()
    @JoinColumn(name = "court_id")
    private Court court;

    @ManyToOne()
    @JoinColumn(name = "referee_id")
    private Referee referee;

}
