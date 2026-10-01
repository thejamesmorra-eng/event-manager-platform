package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.request.EventCreateRequestDto;
import dev.sorokin.eventmanager.dto.request.EventUpdateRequestDto;
import dev.sorokin.eventmanager.dto.response.EventDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.exception.InvalidRequestException;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.model.EventStatus;
import dev.sorokin.eventmanager.model.UserRole;
import dev.sorokin.eventmanager.repository.EventRepository;
import dev.sorokin.eventmanager.repository.LocationRepository;
import dev.sorokin.eventmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final PermissionService permissionService;

    @Transactional
    public EventDto createEvent(EventCreateRequestDto request) {
        String ownerLogin = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserEntity ownerEntity = userRepository.findByLogin(ownerLogin)
                .orElseThrow(() -> new UsernameNotFoundException("User with login '" + ownerLogin + "' was not found"));

        LocationEntity locationEntity = locationRepository.findById(request.locationId())
                .orElseThrow(() -> new EntityNotFoundException("Location entity with id: %s not found"
                        .formatted(request.locationId())));

        if (request.maxPlaces() > locationEntity.getCapacity()) {
            throw new InvalidRequestException("Specified event max places (%s) exceed location capacity (%s)"
                    .formatted(request.maxPlaces(), locationEntity.getCapacity()));
        }

        List<EventEntity> existingEvents = eventRepository.findAllByLocationId(request.locationId());
        for (EventEntity existingEvent : existingEvents) {

            LocalDateTime existingEventStart = existingEvent.getStartAt();
            LocalDateTime existingEventEnd = existingEvent.getStartAt().plusMinutes(existingEvent.getDurationMinutes());

            LocalDateTime requestEventStart = request.date();
            LocalDateTime requestEventEnd = request.date().plusMinutes(request.duration());

            if (existingEventStart.isBefore(requestEventEnd) && requestEventStart.isBefore(existingEventEnd)) {
                throw new InvalidRequestException("Event overlaps with existing event (id=%s) on this location"
                                .formatted(existingEvent.getId()));
            }
        }

        EventEntity eventEntity = new EventEntity(
                request.name(),
                request.date(),
                request.duration(),
                request.maxPlaces(),
                0,
                request.cost(),
                EventStatus.WAIT_START,
                locationEntity,
                ownerEntity
        );
        return eventMapper.toResponse(eventRepository.save(eventEntity));
    }

    @Transactional
    public void cancelEvent(Long id) {
        EventEntity eventEntity = getEntityOrThrow(id);

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        UserRole role = UserRole.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .iterator().next().getAuthority().replace("ROLE_", ""));

        permissionService.checkPermissionOrThrow(eventEntity, login, role);

        if (eventEntity.getStatus() != EventStatus.WAIT_START) {
            throw new InvalidRequestException(
                    "Cannot cancel event with status: %s. Only WAIT_START events can be cancelled"
                            .formatted(eventEntity.getStatus())
            );
        }

        eventEntity.setStatus(EventStatus.CANCELLED);
    }

    @Transactional(readOnly = true)
    public EventDto getEventById(Long id) {
        EventEntity eventEntity = getEntityOrThrow(id);
        return eventMapper.toResponse(eventEntity);
    }

    @Transactional
    public EventDto updateEvent(Long id, EventUpdateRequestDto request) {
        EventEntity eventEntity = getEntityOrThrow(id);

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        UserRole role = UserRole.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .iterator().next().getAuthority().replace("ROLE_", ""));

        permissionService.checkPermissionOrThrow(eventEntity, login, role);

        if (request.isEmpty()) {
            return eventMapper.toResponse(eventEntity);
        }

        if (request.name() != null) eventEntity.setName(request.name());
        if (request.maxPlaces() != null) eventEntity.setMaxPlaces(request.maxPlaces());
        if (request.date() != null) eventEntity.setStartAt(request.date());
        if (request.cost() != null) eventEntity.setCost(request.cost());
        if (request.duration() != null) eventEntity.setDurationMinutes(request.duration());
        if (request.locationId() != null) {
            LocationEntity newLocation = locationRepository.findById(request.locationId())
                    .orElseThrow(() -> new EntityNotFoundException("Location entity with id: %s not found"
                            .formatted(request.locationId())));
            eventEntity.setLocation(newLocation);
        }

        if (eventEntity.getMaxPlaces() < eventEntity.getOccupiedPlaces()) {
            throw new InvalidRequestException("maxPlaces cannot be less than occupiedPlaces");
        }
        if (eventEntity.getMaxPlaces() > eventEntity.getLocation().getCapacity()) {
            throw new InvalidRequestException("maxPlaces exceeds location capacity");
        }


        // ==========================Это надо вынести в общий метод=====================================================
        List<EventEntity> existingEvents = eventRepository.findAllByLocationId(eventEntity.getLocation().getId());
        for (EventEntity existingEvent : existingEvents) {

            LocalDateTime existingEventStart = existingEvent.getStartAt();
            LocalDateTime existingEventEnd = existingEvent.getStartAt().plusMinutes(existingEvent.getDurationMinutes());

            LocalDateTime newEventStart = eventEntity.getStartAt();
            LocalDateTime newEventEnd = eventEntity.getStartAt().plusMinutes(eventEntity.getDurationMinutes());

            if (existingEventStart.isBefore(newEventEnd) && newEventStart.isBefore(existingEventEnd)) {
                throw new InvalidRequestException("Event overlaps with existing event (id=%s) on this location"
                        .formatted(existingEvent.getId()));
            }
        }
        // =============================================================================================================

        return eventMapper.toResponse(eventEntity);
    }

    private EventEntity getEntityOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event entity with id: %s not found".formatted(id)));
    }

    private void checkOverlap(Long locationId, LocalDateTime newEventStart, Integer newEventDuration, Long newEventId) {

    }
}