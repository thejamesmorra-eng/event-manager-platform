package dev.sorokin.eventmanager.exception;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        String localDateTime
) {
}
