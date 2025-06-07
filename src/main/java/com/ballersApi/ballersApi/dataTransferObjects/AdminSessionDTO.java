package com.ballersApi.ballersApi.dataTransferObjects;

import com.ballersApi.ballersApi.models.Court;
import com.ballersApi.ballersApi.models.SessionType;
import com.ballersApi.ballersApi.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AdminSessionDTO {
    private Long id;
    private SessionType type;
    private LocalDate matchDate;
    private String matchStartTime;
    private String matchEndTime;
    private int maxPlayers;
    private double price;
    private long courtId;
    private long refereeId;
}
