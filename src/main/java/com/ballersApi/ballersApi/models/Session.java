package com.ballersApi.ballersApi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


  @NotNull(message = "Session type can't be empty")
    @Column(nullable = false)
    private SessionType type;


    @NotNull(message = "matchDate can't be null")
    @Column(nullable = false)
    // Time stamp for the date
    private LocalDate matchDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @NotNull(message = "matchStartTime can't be null")
    @Column(nullable = false)
    // Time stamp for the start of the session
    private LocalTime matchStartTime;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @NotNull(message = "matchEndTime can't be null")
    @Column(nullable = false)
    // Time stamp for the End of the session
    private LocalTime matchEndTime;


   @NotNull(message = "maxPlayers can't be null")
    @Column(nullable = false)
   @Min(value = 2, message = "Max players must be at least 2")
   @Max(value = 10, message = "Max players cannot exceed 10")
    private int maxPlayers;

    @NotNull(message = "price can't be null")
    private double price;
    @Max(value = 10, message = "Max players cannot exceed 12")
    private int playerCount;

    @ManyToOne()
    @JoinColumn(name = "court_id")
    private Court court;

    @ManyToOne()
    @JoinColumn(name = "referee_id")
    private Referee referee;



}
