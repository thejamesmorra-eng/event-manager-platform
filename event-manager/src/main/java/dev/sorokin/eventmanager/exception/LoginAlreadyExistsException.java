package dev.sorokin.eventmanager.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
