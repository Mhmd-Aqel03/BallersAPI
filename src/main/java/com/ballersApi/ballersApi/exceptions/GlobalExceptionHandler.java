package com.ballersApi.ballersApi.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //  This Handles all "Non Custom" error, or internal server errors.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());

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


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionCreationException.class)
    public ResponseEntity<String> handleSessionCreationException(SessionCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSessionNotFoundException(SessionNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
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

        response.put("message", "Invalid Token: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationFailedException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong with authenticating the User: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorizationFailedException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong with authorization: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CodeVerificationException.class)
    public ResponseEntity<Map<String, Object>> handleEmailCodeVerificationException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong with Verifying Email: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameAlreadyTakenException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorizationDeniedException(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong with authorization, make sure you have an 'Authorization header set': " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(PlayerAlreadyInTeamException.class)
    public ResponseEntity<Map<String, String>> handlePlayerAlreadyInTeam(PlayerAlreadyInTeamException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CourtIdNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCourtIdNotFoundException(CourtIdNotFoundException e) {
        Map<String, Object> response = new HashMap<>();

        response.put("msg", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoCourtsFoundException.class)
    public ResponseEntity<String> handleNoCourtsFoundException(NoCourtsFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourtImageIdNotFoundException.class)
    public ResponseEntity<String> handleCourtImageIdNotFoundException(CourtImageIdNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvitationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleInvitationNotFoundException(InvitationNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TeamFullException.class)
    public ResponseEntity<Map<String, String>> handleTeamFullException(TeamFullException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(WrongInvitationTypeException.class)
    public ResponseEntity<Map<String, String>> handleWrongInvitaionType(WrongInvitationTypeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotEnoughPlayersException.class)
    public ResponseEntity<Map<String, String>> handleNotEnoughPlayers(NotEnoughPlayersException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CanNotFetchDataException.class)
    public ResponseEntity<Map<String, String>> handleCanNotFetchData(CanNotFetchDataException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SessionDeletionException.class)
    public ResponseEntity<Map<String, String>> handleSessionDeletionException(SessionDeletionException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TeamSessionCreationException.class)
    public ResponseEntity<Map<String, String>> handleTeamSessionCreationException(TeamSessionCreationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TeamSessionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTeamSessionNotFoundException(TeamSessionNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RespondedToInviteException.class)
    public ResponseEntity<Map<String, String>> handleRespondedToInviteException(RespondedToInviteException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);


    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<Map<String, Object>> handleEmailSendingException(EmailSendingException ex) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("Something went wrong with the email Service: " + ex.getMessage());
        response.put("message", "Something went wrong with the email Service: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(NoResourceFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoint doesn't exist, maybe check your spelling: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "HTTP Method not supported: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);

    }
}
