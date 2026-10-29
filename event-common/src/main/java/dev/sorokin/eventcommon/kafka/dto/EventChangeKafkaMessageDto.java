package dev.sorokin.eventcommon.kafka.dto;

import dev.sorokin.eventcommon.kafka.enums.EventType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record EventChangeKafkaMessageDto(
        UUID messageId,
        EventType eventType,
        Long eventId,
        Instant occurredAt,
        Long ownerId,
        Long changedById,
        List<Long> subscribers,
        List<ChangeItemDto> changes

) {
    public EventChangeKafkaMessageDto(
            EventType eventType,
            Long eventId,
            Long ownerId,
            Long changedById,
            List<Long> subscribers,
            List<ChangeItemDto> changes
    ) {
        this(UUID.randomUUID(),
                eventType,
                eventId,
                Instant.now(),
                ownerId,
                changedById,
                subscribers,
                changes);
    }
}
