package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.request.LocationRequest;
import dev.sorokin.eventmanager.dto.response.LocationResponse;
import dev.sorokin.eventmanager.dto.response.PageResponse;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.exception.InvalidRequestException;
import dev.sorokin.eventmanager.exception.LocationAlreadyExistsException;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.model.EventStatus;
import dev.sorokin.eventmanager.repository.EventRepository;
import dev.sorokin.eventmanager.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final LocationMapper locationMapper;

    @Transactional(readOnly = true)
    public PageResponse<LocationResponse> getAllLocations(Pageable pageable) {
        Page<LocationResponse> page = locationRepository.findAll(pageable)
                .map(locationMapper::toResponse);
        return PageResponse.fromPage(page);
    }

    @Transactional(readOnly = true)
    public LocationResponse getLocationById(Long id) {
        LocationEntity locationEntity = getEntityOrThrow(id);
        return locationMapper.toResponse(locationEntity);
    }

    @Transactional
    public LocationResponse createLocation(LocationRequest request) {
        if (isLocationExists(request.name(), request.address())) {
            throw new LocationAlreadyExistsException(
                    "Location: %s with address: %s is already exists".formatted(request.name(), request.address())
            );
        }
        LocationEntity locationEntity = locationRepository.save(locationMapper.toEntity(request));
        return locationMapper.toResponse(locationEntity);
    }

    @Transactional
    public void deleteLocation(Long id) {
        LocationEntity locationEntity = getEntityOrThrow(id);

        if (eventRepository.existsByLocationId(id)) {
            throw new InvalidRequestException("Location with id: %s has events. Deletion not allowed"
                    .formatted(id));
        }
        locationRepository.delete(locationEntity);
    }

    @Transactional
    public LocationResponse updateLocation(Long id, LocationRequest request) {
        LocationEntity locationEntity = getEntityOrThrow(id);

        if (!(locationEntity.getName().equals(request.name()) && locationEntity.getAddress().equals(request.address()))) {
            if (isLocationExists(request.name(), request.address())) {
                throw new LocationAlreadyExistsException(
                        "Location: %s with address: %s is already exists".formatted(request.name(), request.address())
                );
            }
        }

        int maxEventPlaces = eventRepository.findMaxPlacesByLocationId(
                id,
                List.of(EventStatus.WAIT_START, EventStatus.STARTED)
        );
        if (request.capacity() < maxEventPlaces) {
            throw new InvalidRequestException(
                    "The location’s capacity (%s) cannot be less than the capacity of the largest event (%s)."
                            .formatted(request.capacity(), maxEventPlaces));
        }

        locationMapper.updateEntity(request, locationEntity);
        return locationMapper.toResponse(locationEntity);
    }

    @Transactional(readOnly = true)
    LocationEntity getEntityOrThrow(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location entity with id: %s not found".formatted(id)));
    }

    private boolean isLocationExists(String name, String address) {
        return locationRepository.existsByNameAndAddress(name, address);
    }
}
