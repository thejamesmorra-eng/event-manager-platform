package dev.sorokin.eventmanager.dto.request;

import jakarta.validation.constraints.*;

public record LocationRequest(
        @NotBlank(message = "The location name cannot be empty")
        @Size(min = 2, max = 100, message = "The location name must contain between 2 and 100 characters")
        String name,

        @NotBlank(message = "The location address cannot be empty")
        @Size(min = 2, max = 100, message = "The location address must contain between 2 and 100 characters")
        String address,

        @NotNull(message = "The location capacity is not specified")
        @Positive(message = "The capacity of the location must be greater than 0")
        @Max(value = 100_000, message = "The capacity cannot exceed 100,000")
        Integer capacity,

        @NotBlank(message = "No description of the location is provided")
        @Size(min = 2, max = 500, message = "The location description should contain between 2 and 500 characters")
        String description
) {
}
