package com.ballersApi.ballersApi.models;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private int playerCount = 0;

    @ManyToOne()
    @JoinColumn(name = "court_id")
    private Court court;

    @ManyToOne()
    @JoinColumn(name = "referee_id")
    private User referee;


    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private Chat chat;

}



