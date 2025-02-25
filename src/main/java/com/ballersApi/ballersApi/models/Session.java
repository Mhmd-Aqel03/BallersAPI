package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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


    @NotNull
    @Column(nullable = false)
    // Time stamp for the date and time
    private LocalDateTime matchDateTime;


   @NotNull
    @Column(nullable = false)
   @Min(value = 6, message = "Max players must be at least 2")
   @Max(value = 12, message = "Max players cannot exceed 12")
    private int maxPlayers;

    private int playerCount;

    @ManyToOne()
    @JoinColumn(name = "court_id")
    private Court court;

    @ManyToOne()
    @JoinColumn(name = "referee_id")
    private Referee referee;

}
