package com.openclassrooms.mddapi.exception;

public class UserNotFoundException extends RuntimeException {

    private UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("User with email " + email + " not found");
    }

    public static UserNotFoundException byId(Long id) {
        return new UserNotFoundException("User with id " + id + " not found");
    }

    public static UserNotFoundException byField(String field, String value) {
        return new UserNotFoundException("User with " + field + " " + value + " not found");
    }
}