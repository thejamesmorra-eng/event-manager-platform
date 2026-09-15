package dev.sorokin.eventmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCredentials(
        @NotBlank(message = "The login cannot be empty")
        @Size(min = 1, max = 255, message = "The login must contain between 1 and 255 characters")
        String login,

        @NotBlank(message = "The password cannot be empty")
        @Size(min = 8, max = 255, message = "The password must contain between 8 and 255 characters")
        String password
) {
}
