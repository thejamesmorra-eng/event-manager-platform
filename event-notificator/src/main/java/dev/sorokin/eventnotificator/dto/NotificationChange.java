package dev.sorokin.eventnotificator.dto;

public record NotificationChange(
        String field,
        String oldValue,
        String newValue
) {
}
