package dev.sorokin.eventmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegistration(
        @NotBlank(message = "The login cannot be empty")
        @Size(min = 1, max = 255, message = "The login must contain between 1 and 255 characters")
        String login,

        @NotBlank(message = "The password cannot be empty")
        @Size(min = 8, max = 255, message = "The password must contain between 8 and 255 characters")
        String password,

        @NotNull(message = "The age is not specified")
        @Min(value = 18, message = "The age cannot be less than 18")
        Integer age
) {
}
