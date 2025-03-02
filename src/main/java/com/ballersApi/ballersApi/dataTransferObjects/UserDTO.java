package com.ballersApi.ballersApi.dataTransferObjects;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String role;
}
