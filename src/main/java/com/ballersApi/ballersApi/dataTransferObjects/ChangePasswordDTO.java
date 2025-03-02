package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotEmpty(message = "No email provided")
    String email;
    @NotEmpty(message = "No new password provided")
    String newPassword;
    @NotEmpty(message = "No code provided")
    String code;
}
