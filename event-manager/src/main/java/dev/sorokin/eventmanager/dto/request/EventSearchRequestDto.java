package dev.sorokin.eventmanager.dto.request;

import dev.sorokin.eventmanager.model.EventStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventSearchRequestDto(
        @Size(min = 1, max = 255, message = "The event name must contain between 1 and 255 characters")
        String name,

        @Positive(message = "The min capacity of the event must be greater than 0")
        Integer placesMin,

        @Positive(message = "The max capacity of the event must be greater than 0")
        @Max(value = 100_000, message = "The max event capacity cannot exceed 100,000")
        Integer placesMax,

        LocalDateTime dateStartAfter,

        LocalDateTime dateStartBefore,

        @Positive(message = "The min event cost must be greater than 0")
        Integer costMin,

        @Positive(message = "The max event cost must be greater than 0")
        Integer costMax,

        @Min(value = 30, message = "The event duration cannot be less than 30")
        Integer durationMin,

        @Min(value = 30, message = "The event duration cannot be less than 30")
        Integer durationMax,

        @Positive(message = "The event location id must be greater than 0")
        Long locationId,

        EventStatus eventStatus
) {
}
