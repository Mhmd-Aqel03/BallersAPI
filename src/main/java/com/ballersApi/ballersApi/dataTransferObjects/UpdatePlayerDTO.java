package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePlayerDTO {
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 7, max = 20, message = "Username must be between 7-20 characters long")
    private String username;

    @NotBlank(message = "Position Can't be empty")
    private String position;

    private String photoUrl;

    @NotBlank(message = "Phone Number Can't be empty")
    private String phoneNumber;
}
