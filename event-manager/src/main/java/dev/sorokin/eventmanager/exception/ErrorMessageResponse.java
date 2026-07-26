package dev.sorokin.eventmanager.exception;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        int statusCode,
        String error,
        String message,
        LocalDateTime localDateTime
) {
}
