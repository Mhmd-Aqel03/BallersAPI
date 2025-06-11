package com.ballersApi.ballersApi.exceptions;

public class PlayerAlreadyEndorsedException extends RuntimeException {
    public PlayerAlreadyEndorsedException(String message) {
        super(message);
    }
}
