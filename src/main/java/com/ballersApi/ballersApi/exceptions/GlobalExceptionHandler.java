package com.ballersApi.ballersApi.exceptions;


import com.ballersApi.ballersApi.models.Court;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CourtIdNotFoundException.class)
    public ResponseEntity<String> handleCourtIdNotFoundException(CourtIdNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoCourtsFoundException.class)
    public ResponseEntity<String> handleNoCourtsFoundException(NoCourtsFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourtImageIdNotFoundException.class)
    public ResponseEntity<String> handleCourtImageIdNotFoundException(CourtImageIdNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
