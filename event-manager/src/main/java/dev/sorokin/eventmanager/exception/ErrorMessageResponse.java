package dev.sorokin.eventmanager.exception;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        int statusCode,
        String message,
        String detailedMessage,
        LocalDateTime localDateTime
) {
}
