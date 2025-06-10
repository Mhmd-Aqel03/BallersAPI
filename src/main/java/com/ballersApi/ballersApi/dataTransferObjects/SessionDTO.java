package com.ballersApi.ballersApi.dataTransferObjects;

import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionType;
import com.ballersApi.ballersApi.models.Team;
import com.ballersApi.ballersApi.services.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SessionDTO {
    private Long id;
    @NotNull(message = "Session type can't be empty")
    private SessionType type;

    @NotNull(message = "matchDate can't be null")
    private LocalDate matchDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")

    @NotNull(message = "matchStartTime can't be null")
    private LocalTime matchStartTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @NotNull(message = "matchEndTime can't be null")
    private LocalTime matchEndTime;

    @Min(value = 2, message = "Max players must be at least 2")
    @Max(value = 10, message = "Max players cannot exceed 10")
    private int maxPlayers;

    @Positive(message = "Price must be greater than 0")
    private double price;


    @Max(value = 10, message = "Max players cannot exceed 12")
    private int playerCount = 0;

    //These are -1 because they'll be 0 by default, so I did this to check if the user send an id or not
    private long courtId = -1;

    private long refereeId = -1;

    private Player mvp;

    private Team winningTeam;
    private SessionTeamDTO teamA;
    private SessionTeamDTO teamB;

    public SessionDTO(Session session, UserService userService) {
        this.id = session.getId();
        this.type = session.getType();
        this.matchDate = session.getMatchDate();
        this.matchStartTime = session.getMatchStartTime();
        this.matchEndTime = session.getMatchEndTime();
        this.maxPlayers = session.getMaxPlayers();
        this.price = session.getPrice();
        this.playerCount = session.getPlayerCount();
        this.winningTeam = session.getWinningTeam();

        this.teamA = new SessionTeamDTO(session.getTeamA(), Team.A, userService);
        this.teamB = new SessionTeamDTO(session.getTeamB(), Team.B, userService);

        this.mvp = session.getMvp();
    }


    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public LocalTime getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(LocalTime matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    public LocalTime getMatchEndTime() {
        return matchEndTime;
    }

    public void setMatchEndTime(LocalTime matchEndTime) {
        this.matchEndTime = matchEndTime;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCourtId() {
        return courtId;
    }

    public void setCourtId(long courtId) {
        this.courtId = courtId;
    }

    public long getRefereeId() {
        return refereeId;
    }

    public void setRefereeeId(long refereeId) {
        this.refereeId = refereeId;
    }
}
