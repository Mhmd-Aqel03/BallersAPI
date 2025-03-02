package com.ballersApi.ballersApi.exceptions;

public class PlayerAlreadyInTeamException extends RuntimeException {
    public PlayerAlreadyInTeamException(String message) {
        super(message);
    }
}
