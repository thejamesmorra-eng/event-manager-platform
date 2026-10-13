package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.request.EventCreateRequestDto;
import dev.sorokin.eventmanager.dto.request.EventSearchRequestDto;
import dev.sorokin.eventmanager.dto.request.EventUpdateRequestDto;
import dev.sorokin.eventmanager.dto.response.EventDto;
import dev.sorokin.eventmanager.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDto> create(@Valid @RequestBody EventCreateRequestDto request) {
        log.debug("Create event: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventId) {
        log.debug("Delete event with id: {}", eventId);
        eventService.cancelEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getById(@PathVariable Long eventId) {
        log.debug("Get event by id: {}", eventId);
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> update(@PathVariable Long eventId, @Valid @RequestBody EventUpdateRequestDto request) {
        log.debug("Update event: {} with id: {}", request, eventId);
        return ResponseEntity.ok(eventService.updateEvent(eventId, request));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> search(@Valid @RequestBody EventSearchRequestDto request) {
        log.debug("Search event by filters: {}", request);
        return ResponseEntity.ok(eventService.searchEvents(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents() {
        log.debug("Get owner events");
        return ResponseEntity.ok(eventService.getMyEvents());
    }
}
