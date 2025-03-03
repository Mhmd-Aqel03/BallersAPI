package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {
    @NotEmpty(message = "Email not provided")
    private String email;
    @NotEmpty(message = "Password not provided")
    private String password;
}
