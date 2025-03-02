package com.ballersApi.ballersApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourtImageIdNotFoundException extends RuntimeException {

    public CourtImageIdNotFoundException(String message) {
        super(message);
    }
}

