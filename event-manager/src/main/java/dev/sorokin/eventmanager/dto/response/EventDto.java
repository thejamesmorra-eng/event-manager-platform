package dev.sorokin.eventmanager.dto.response;

import dev.sorokin.eventmanager.model.EventStatus;

import java.time.LocalDateTime;

public record EventDto(
        Long id,
        String name,
        Long ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        LocalDateTime date,
        Integer cost,
        Integer duration,
        Long locationId,
        EventStatus status
) {
}
