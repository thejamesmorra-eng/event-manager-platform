package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.request.EventCreateRequestDto;
import dev.sorokin.eventmanager.dto.request.EventSearchRequestDto;
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
import dev.sorokin.eventmanager.specification.EventSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

        checkOverlap(locationEntity.getId(), null, request.date(), request.duration());

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

        checkOverlap(eventEntity.getLocation().getId(), id, request.date(), request.duration());

        return eventMapper.toResponse(eventEntity);
    }

    @Transactional(readOnly = true)
    public List<EventDto> searchEvents(EventSearchRequestDto request) {
        if (request.isEmpty()) {
            return eventMapper.toResponseList(eventRepository.findAll());
        }

        Specification<EventEntity> spec = Specification.unrestricted();
        if (request.name() != null) spec = spec.and(EventSpecification.hasName(request.name()));
        if (request.placesMin() != null) spec = spec.and(EventSpecification.placesGreaterThanOrEqual(request.placesMin()));
        if (request.placesMax() != null) spec = spec.and(EventSpecification.placesLessThanOrEqual(request.placesMax()));
        if (request.dateStartAfter() != null) spec = spec.and(EventSpecification.startAtAfter(request.dateStartAfter()));
        if (request.dateStartBefore() != null) spec = spec.and(EventSpecification.startAtAfter(request.dateStartBefore()));
        if (request.costMin() != null) spec = spec.and(EventSpecification.costGreaterThanOrEqual(request.costMin()));
        if (request.costMax() != null) spec = spec.and(EventSpecification.costLessThanOrEqual(request.costMax()));
        if (request.durationMin() != null) spec = spec.and(EventSpecification.durationGreaterThanOrEqual(request.durationMin()));
        if (request.durationMax() != null) spec = spec.and(EventSpecification.durationLessThanOrEqual(request.durationMax()));
        if (request.locationId() != null) spec = spec.and(EventSpecification.hasLocationId(request.locationId()));
        if (request.eventStatus() != null) spec = spec.and(EventSpecification.hasStatus(request.eventStatus()));

        return eventMapper.toResponseList(eventRepository.findAll(spec));
    }

    public List<EventDto> getMyEvents() {
        String ownerLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity ownerEntity = userRepository.findByLogin(ownerLogin)
                .orElseThrow(() -> new UsernameNotFoundException("User with login '" + ownerLogin + "' was not found"));
        return eventMapper.toResponseList(eventRepository.findAllByOwnerId(ownerEntity.getId()));
    }

    private EventEntity getEntityOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event entity with id: %s not found".formatted(id)));
    }

    private void checkOverlap(Long locationId,
                              Long requestEventId,
                              LocalDateTime requestEventStart,
                              Integer requestEventDuration) {
        List<EventEntity> existingEvents = eventRepository.findAllByLocationIdAndStatusIn(
                locationId,
                List.of(EventStatus.STARTED, EventStatus.WAIT_START));

        for (EventEntity existingEvent : existingEvents) {

            if (existingEvent.getId().equals(requestEventId)) {
                continue;
            }

            LocalDateTime existingEventStart = existingEvent.getStartAt();
            LocalDateTime existingEventEnd = existingEvent.getStartAt().plusMinutes(existingEvent.getDurationMinutes());
            LocalDateTime newEventEnd = requestEventStart.plusMinutes(requestEventDuration);

            if (existingEventStart.isBefore(newEventEnd) && requestEventStart.isBefore(existingEventEnd)) {
                throw new InvalidRequestException("Event overlaps with existing event (id=%s) on this location"
                        .formatted(existingEvent.getId()));
            }
        }
    }
}