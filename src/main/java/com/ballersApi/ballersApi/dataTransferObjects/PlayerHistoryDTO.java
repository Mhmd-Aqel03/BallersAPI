package com.ballersApi.ballersApi.dataTransferObjects;

import lombok.Data;

@Data
public class PlayerHistoryDTO {
    private String result;
    private SessionDTO sessionDetails;

    // Constructor to accept the sessionDTO directly
    public PlayerHistoryDTO(SessionDTO sessionDetails, boolean won) {
        this.result = won ? "won" : "lost";
        this.sessionDetails = sessionDetails;
    }
}
