package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UsernameDTO {
    @NotEmpty(message = "No username Provided")
    private String username;
}
