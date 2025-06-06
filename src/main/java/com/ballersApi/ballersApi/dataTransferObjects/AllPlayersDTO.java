package com.ballersApi.ballersApi.dataTransferObjects;

import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.services.UserService;
import lombok.Data;

@Data
public class AllPlayersDTO {
    private Long playerId;
    private String username;
    private String postion;
    private String photoUrl;

    public AllPlayersDTO(Player player, UserService userService) {
        this.playerId = player.getId();
        this.username = userService.getUserByPlayerId(player.getId()).getUsername();
        this.postion = player.getPostion();
        this.photoUrl = player.getPhotoUrl();
    }
}
