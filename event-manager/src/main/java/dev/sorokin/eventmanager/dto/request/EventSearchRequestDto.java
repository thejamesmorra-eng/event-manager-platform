package dev.sorokin.eventmanager.dto.request;

import dev.sorokin.eventmanager.model.EventStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventSearchRequestDto(
        @Size(min = 1, max = 255, message = "The event name must contain between 1 and 255 characters")
        @Pattern(regexp = ".*\\S.*", message = "The event name must contain at least one non-whitespace character")
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
        @AssertTrue(message = "placesMin cannot be greater than placesMax")
        public boolean isPlacesRangeValid() {
                if (placesMin == null || placesMax == null) {
                        return true;
                }
                return placesMin <= placesMax;
        }

        @AssertTrue(message = "dateStartAfter cannot be after dateStartBefore")
        public boolean isDateRangeValid() {
                if (dateStartAfter == null || dateStartBefore == null) {
                        return true;
                }
                return !dateStartAfter.isAfter(dateStartBefore);
        }

        @AssertTrue(message = "costMin cannot be greater than costMax")
        public boolean isCostRangeValid() {
                if (costMin == null || costMax == null) {
                        return true;
                }
                return costMin <= costMax;
        }

        @AssertTrue(message = "durationMin cannot be greater than durationMax")
        public boolean isDurationRangeValid() {
                if (durationMin == null || durationMax == null) {
                        return true;
                }
                return durationMin <= durationMax;
        }

        public boolean isEmpty() {
                return name == null
                        && placesMin == null
                        && placesMax == null
                        && dateStartAfter == null
                        && dateStartBefore == null
                        && costMin == null
                        && costMax == null
                        && durationMin == null
                        && durationMax == null
                        && locationId == null
                        && eventStatus == null;
        }
}
