package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VerifyCodeDTO {
    @NotEmpty(message = "No username provided")
    private String username;
    @NotEmpty(message = "No verification code provided")
    private String code;
}
