package com.ballersApi.ballersApi.exceptions;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(Long chatId) {
        super("Chat with ID " + chatId + " not found.");
    }
}
