package com.ballersApi.ballersApi.dataTransferObjects;

import com.ballersApi.ballersApi.models.Session;
import com.ballersApi.ballersApi.models.SessionType;
import com.ballersApi.ballersApi.models.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class PlayerHistoryDTO {
    private String result;
    private SessionSummary sessionDetails;

    public PlayerHistoryDTO(Session session, boolean won, String mvpUsername) {
        this.result = won ? "won" : "lost";
        this.sessionDetails = new SessionSummary(
                session.getId(),
                session.getType(),
                session.getMatchDate(),
                session.getPrice(),
                mvpUsername,
                session.getWinningTeam()
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SessionSummary {
        private Long id;
        private SessionType type;
        private LocalDate matchDate;
        private double price;
        private String mvpUsername;
        private Team winningTeam;
    }
}
