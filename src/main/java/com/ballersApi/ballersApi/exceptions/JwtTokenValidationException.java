package com.ballersApi.ballersApi.exceptions;

public class JwtTokenValidationException extends RuntimeException {
    public JwtTokenValidationException(String message) {
        super(message);
    }
}
