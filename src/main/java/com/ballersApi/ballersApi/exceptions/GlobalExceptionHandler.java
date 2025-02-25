package com.ballersApi.ballersApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //  This Handles all "Non Custom" error, or internal server errors.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong: " + ex.getClass().getSimpleName() + " - " +ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTokenException(InvalidTokenException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //  This Handles Custom Exception, User not found in this case.
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //  User creation Error, wasn't sure what to call it.
    @ExceptionHandler(UserCreationErrorException.class)
    public ResponseEntity<Map<String, Object>> handleUserCreationErrorException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // This Handles validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            response.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // I'll use this exception as a "shit went wrong with the DB"
    @ExceptionHandler(DatabaseConnectionErrorException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseConnectionErrorException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtTokenValidationException.class)
    public ResponseEntity<Map<String, Object>> handleJwtTokenValidationException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message","Invalid Token: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationFailedException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message","Something went wrong with authenticating the User: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorizationFailedException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong with authorization: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
