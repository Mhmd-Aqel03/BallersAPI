package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotEmpty(message = "No username provided")
    String username;
    @NotEmpty(message = "No new password provided")
    String newPassword;
    @NotEmpty(message = "No code provided")
    String code;
}
