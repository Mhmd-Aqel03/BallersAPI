package com.ballersApi.ballersApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourtIdNotFoundException extends RuntimeException {

    public CourtIdNotFoundException(String message) {
        super(message);
    }
}
