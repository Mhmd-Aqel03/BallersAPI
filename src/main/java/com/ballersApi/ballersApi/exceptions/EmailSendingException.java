package com.ballersApi.ballersApi.exceptions;

public class EmailSendingException extends RuntimeException {
  public EmailSendingException(String message) {
    super(message);
  }
}
