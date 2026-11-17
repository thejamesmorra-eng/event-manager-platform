package dev.sorokin.eventmanager.exception;

public class OutboxSerializationException extends RuntimeException {
    public OutboxSerializationException(String message) {
        super(message);
    }
}
