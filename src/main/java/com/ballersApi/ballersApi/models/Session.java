package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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


    @NotBlank(message = "Session type can't be empty")
    @Column(nullable = false)
    private String type;

    @NotNull(message = "matchDateTime can't be null")
    @Column(nullable = false)
    // Time stamp for the date and time
    private LocalDateTime matchDateTime;

    @NotNull(message = "maxPlayers can't be null")
    @Column(nullable = false)
    @Min(value = 6, message = "Max players must be at least 6")
    @Max(value = 12, message = "Max players cannot exceed 12")
    private int maxPlayers;

    @NotNull(message = "price can't be null")
    private double price;
    @Max(value = 12, message = "Max players cannot exceed 12")
    private int playerCount = 0;

    @ManyToOne()
    @JoinColumn(name = "court_id")
    private Court court;

    @ManyToOne()
    @JoinColumn(name = "referee_id")
    private Referee referee;

}
