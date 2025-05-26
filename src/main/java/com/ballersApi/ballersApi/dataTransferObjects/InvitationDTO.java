package com.ballersApi.ballersApi.dataTransferObjects;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class InvitationDTO {
    private Long inviteId;
    private Long sessionId;
    private LocalDate sessionDate;
    private String senderUsername;
    private boolean status;
    private String sendAt;  // Already formatted string

    public InvitationDTO(Long inviteId, Long sessionId, LocalDate sessionDate, String senderUsername, boolean status, LocalDateTime sendAt) {
        this.inviteId = inviteId;
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
        this.senderUsername = senderUsername;
        this.status = status;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE 'at' hh:mm a");
        this.sendAt = sendAt.format(formatter);
    }
}
