package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshTokenDTO {
    @NotEmpty( message = "No Refresh Token provided.")
    private String token;
}
