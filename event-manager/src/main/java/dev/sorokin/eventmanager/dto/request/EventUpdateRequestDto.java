package dev.sorokin.eventmanager.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventUpdateRequestDto(
        @Size(min = 1, max = 255, message = "The event name must contain between 1 and 255 characters")
        String name,

        @Positive(message = "The capacity of the event must be greater than 0")
        @Max(value = 100_000, message = "The event capacity cannot exceed 100,000")
        Integer maxPlaces,

        @Future(message = "The event date must be in the future")
        LocalDateTime date,

        @Positive(message = "The event cost must be greater than 0")
        Integer cost,

        @Min(value = 30, message = "The event duration cannot be less than 30")
        Integer duration,

        @Positive(message = "The event location id must be greater than 0")
        Long locationId
) {
}
