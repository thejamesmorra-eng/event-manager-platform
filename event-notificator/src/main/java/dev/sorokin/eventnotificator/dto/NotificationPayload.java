package dev.sorokin.eventnotificator.dto;

import dev.sorokin.eventcommon.kafka.enums.EventType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record NotificationPayload(
        UUID messageId,
        EventType eventType,
        Instant occurredAt,
        Long changeById,
        Long ownerId,
        String eventName,
        List<NotificationChange> changes
) {
}
