package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class FavouriteListDTO {
    private long userId;
    private String username;
    private long playerId;
}
