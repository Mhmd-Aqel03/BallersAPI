package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlayerDTO {
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 7, max = 20, message = "Username must be between 7-20 characters long")
    private String username;

    @NotEmpty(message = "Password can't be empty")
    private String password;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Phone Number Can't be empty")
    private String phoneNumber;

    @NotBlank(message = "Position Can't be empty")
    private String position;

    private String photoUrl;
}
