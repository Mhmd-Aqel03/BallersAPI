package com.ballersApi.ballersApi.dataTransferObjects;

import lombok.Data;

@Data
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
