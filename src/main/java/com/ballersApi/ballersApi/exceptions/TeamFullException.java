package com.ballersApi.ballersApi.exceptions;

public class TeamFullException extends RuntimeException {
    public TeamFullException(String message) {
        super(message);
    }
}
