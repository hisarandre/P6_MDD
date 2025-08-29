package com.openclassrooms.mddapi.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User with email or username already exists");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
