package com.ballersApi.ballersApi.exceptions;

public class PlayerNotInTeamException extends RuntimeException {
    public PlayerNotInTeamException(String message) {
        super(message);
    }
}
