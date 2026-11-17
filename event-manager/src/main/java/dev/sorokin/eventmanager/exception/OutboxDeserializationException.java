package dev.sorokin.eventmanager.exception;

public class OutboxDeserializationException extends RuntimeException {
    public OutboxDeserializationException(String message) {
        super(message);
    }
}
