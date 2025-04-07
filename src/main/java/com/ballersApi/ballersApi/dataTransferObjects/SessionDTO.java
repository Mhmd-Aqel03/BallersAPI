package com.ballersApi.ballersApi.dataTransferObjects;

import com.ballersApi.ballersApi.models.SessionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SessionDTO {
    @NotNull(message = "Session type can't be empty")
    private SessionType type;

    @NotNull(message = "matchDate can't be null")
    private LocalDate matchDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @NotNull(message = "matchStartTime can't be null")
    private LocalTime matchStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    private LocalTime matchEndTime;

    @NotNull(message = "maxPlayers can't be null")
    @Min(value = 2, message = "Max players must be at least 2")
    @Max(value = 10, message = "Max players cannot exceed 10")
    private int maxPlayers;

    @NotNull(message = "price can't be null")
    private double price;

    @Max(value = 10, message = "Max players cannot exceed 12")
    private int playerCount = 0;

    //These are -1 because they'll be 0 by default, so I did this to check if the user send an id or not
    private long courtId = -1;

    private long refereeId = -1;
}
