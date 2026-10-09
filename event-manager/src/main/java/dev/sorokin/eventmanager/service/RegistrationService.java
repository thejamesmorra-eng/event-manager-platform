package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.response.EventDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.exception.InvalidRequestException;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.model.EventStatus;
import dev.sorokin.eventmanager.repository.RegistrationRepository;
import dev.sorokin.eventmanager.security.SecurityContextService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EventService eventService;
    private final UserService userService;
    private final SecurityContextService securityContextService;
    private final RegistrationRepository registrationRepository;
    private final EventMapper eventMapper;

    @Transactional
    public void registerUserOnEvent(Long eventId) {
        String userLogin = securityContextService.getCurrentLogin();
        UserEntity userEntity = userService.getEntityOrThrow(userLogin);

        EventEntity eventEntity = eventService.getEventByIdForUpdate(eventId);

        if (eventEntity.getStatus() != EventStatus.WAIT_START) {
            throw new InvalidRequestException(
                    "Cannot register on event with status: %s. Only WAIT_START events allow registration"
                            .formatted(eventEntity.getStatus())
            );
        }
        if (eventEntity.getOccupiedPlaces() >= eventEntity.getMaxPlaces()) {
            throw new InvalidRequestException(
                    "Cannot register on event: no available places (occupied: %s, max places: %s)"
                            .formatted(eventEntity.getOccupiedPlaces(), eventEntity.getMaxPlaces())
            );
        }
        if (registrationRepository.existsByEventIdAndUserId(eventEntity.getId(), userEntity.getId())) {
            throw new InvalidRequestException(
                    "Cannot register on event with id: %s. You are already registered"
                            .formatted(eventEntity.getId())
            );
        }

        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() + 1);

        RegistrationEntity registrationEntity = new RegistrationEntity(
                eventEntity,
                userEntity
        );

        registrationRepository.save(registrationEntity);
    }

    @Transactional
    public void cancelRegistration(Long eventId) {
        String userLogin = securityContextService.getCurrentLogin();
        UserEntity userEntity = userService.getEntityOrThrow(userLogin);

        EventEntity eventEntity = eventService.getEventByIdForUpdate(eventId);

        if (eventEntity.getStatus() != EventStatus.WAIT_START) {
            throw new InvalidRequestException(
                    "Cannot cancel registration on event with status: %s. Only WAIT_START events allow cancellation"
                            .formatted(eventEntity.getStatus())
            );
        }

        RegistrationEntity registrationEntity = registrationRepository.findByEventIdAndUserId(
                eventEntity.getId(), userEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Registration on event with id: %s not found".formatted(eventId)));

        registrationRepository.delete(registrationEntity);

        if (eventEntity.getOccupiedPlaces() > 0) {
            eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() - 1);
        }
    }


    @Transactional(readOnly = true)
    public List<EventDto> getMyRegistrations() {
        String userLogin = securityContextService.getCurrentLogin();
        UserEntity userEntity = userService.getEntityOrThrow(userLogin);

        List<RegistrationEntity> registrations = registrationRepository.findAllByUserId(userEntity.getId());

        return eventMapper.toResponseList(registrations.stream()
                .map(RegistrationEntity::getEvent)
                .toList());
    }
}
