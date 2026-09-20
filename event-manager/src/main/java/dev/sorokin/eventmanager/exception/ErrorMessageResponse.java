package dev.sorokin.eventmanager.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        String dateTime
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static ErrorMessageResponse of(String message, String detailedMessage) {
        return new ErrorMessageResponse(
                message,
                detailedMessage,
                LocalDateTime.now().format(FORMATTER)
        );
    }
}
