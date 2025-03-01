package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailDTO {
    @NotEmpty(message = "No email provided")
    private String email;
}
