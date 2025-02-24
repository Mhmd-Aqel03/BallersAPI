package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {
    @NotEmpty(message = "Username not provided")
    private String username;
    @NotEmpty(message = "Password not provided")
    private String password;
}
