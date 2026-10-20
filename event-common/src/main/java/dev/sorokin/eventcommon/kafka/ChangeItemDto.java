package dev.sorokin.eventcommon.kafka;

public record ChangeItemDto(
        String field,
        String oldValue,
        String newValue
) {
}
