package com.ballersApi.ballersApi.dataTransferObjects;

import lombok.Data;

@Data
public class PlayerDTO {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String position;
    private String photoUrl;
}
