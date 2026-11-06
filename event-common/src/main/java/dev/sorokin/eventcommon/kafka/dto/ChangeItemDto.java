package dev.sorokin.eventcommon.kafka.dto;

public record ChangeItemDto(
        String field,
        String oldValue,
        String newValue
) {
}
