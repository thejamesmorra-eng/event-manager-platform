package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.response.EventDto;
import dev.sorokin.eventmanager.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> register(@PathVariable Long eventId) {
        log.debug("Register on event with id: {}", eventId);
        registrationService.registerUserOnEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long eventId) {
        log.debug("Cancel registration on event with id: {}", eventId);
        registrationService.cancelRegistration(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyRegistrations() {
        log.debug("Get user registrations");
        return ResponseEntity.ok(registrationService.getMyRegistrations());
    }
}
