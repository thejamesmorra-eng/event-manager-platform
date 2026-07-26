package dev.sorokin.eventmanager.dto.request;

import jakarta.validation.constraints.*;

public record LocationRequest(
        @NotBlank(message = "Название мероприятия не может быть пустым")
        @Size(min = 2, max = 100, message = "Название мероприятия должно содержать от двух до ста символов")
        String name,

        @NotBlank(message = "Адрес мероприятия не может быть пустым")
        @Size(min = 2, max = 100, message = "Адрес мероприятия должен содержать от двух до ста символов")
        String address,

        @NotNull
        @Positive(message = "Вместимость мероприятия должна быть больше нуля")
        @Max(value = 100_000, message = "Вместимость не может быть более ста тысяч")
        Integer capacity,

        @NotBlank(message = "Описание мероприятия обязательно")
        @Size(min = 2, max = 500, message = "Адрес мероприятия должен содержать от двух до пятисот символов")
        String description
) {
}
