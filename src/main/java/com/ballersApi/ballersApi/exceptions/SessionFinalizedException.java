package com.ballersApi.ballersApi.exceptions;

public class SessionFinalizedException extends RuntimeException {
    public SessionFinalizedException(String message) {
        super(message);
    }
}
