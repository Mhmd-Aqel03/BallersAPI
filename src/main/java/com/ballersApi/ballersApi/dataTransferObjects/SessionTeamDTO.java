package com.ballersApi.ballersApi.dataTransferObjects;

import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.SessionTeam;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data

public class SessionTeamDTO {
    private Long id;
    private List<Long> players;
    private Long sessionId;
    public SessionTeamDTO(SessionTeam team) {
        this.id = team.getId();
        this.sessionId = team.getSession().getId();

        // Make sure we're mapping ALL players, not just the first one
        if (team.getPlayers() != null) {
            // If you want full player information
            this.players = team.getPlayers().stream()
                .map(Player::getId)
                .collect(Collectors.toList());
        } else {
            this.players = new ArrayList<>();
        }
    }
}
