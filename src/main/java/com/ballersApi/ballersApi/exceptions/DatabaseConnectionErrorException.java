package com.ballersApi.ballersApi.exceptions;

public class DatabaseConnectionErrorException extends RuntimeException {
    public DatabaseConnectionErrorException(String message) {
        super(message);
    }
}
