package com.ballersApi.ballersApi.dataTransferObjects;

import com.ballersApi.ballersApi.models.Session;
import lombok.Data;


@Data
public class PlayerHistoryDTO {
    private String result;
    private SessionDTO sessionDetails;

    public PlayerHistoryDTO(Session session, boolean won) {
        this.result = won ? "won" : "lost";
        this.sessionDetails = new SessionDTO(session);
    }
}