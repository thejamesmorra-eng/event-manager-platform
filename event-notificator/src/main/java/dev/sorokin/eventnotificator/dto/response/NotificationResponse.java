package dev.sorokin.eventnotificator.dto.response;

import dev.sorokin.eventcommon.kafka.enums.EventType;
import dev.sorokin.eventnotificator.dto.NotificationPayload;

import java.time.Instant;

public record NotificationResponse(
        Long notificationId,
        EventType type,
        Long eventId,
        Instant createdAt,
        Boolean isRead,
        String message,
        NotificationPayload payload
) {
}
