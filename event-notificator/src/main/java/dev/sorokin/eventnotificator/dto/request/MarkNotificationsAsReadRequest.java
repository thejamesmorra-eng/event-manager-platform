package dev.sorokin.eventnotificator.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record MarkNotificationsAsReadRequest(
        @NotEmpty(message = "List of notifications to read cannot be empty")
        List<Long> notificationIds
) {
}
