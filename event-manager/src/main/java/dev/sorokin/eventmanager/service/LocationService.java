package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.request.LocationRequest;
import dev.sorokin.eventmanager.dto.response.LocationResponse;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    // Пагинация
    public List<LocationResponse> getAllLocations() {
        return locationMapper.toResponseList(locationRepository.findAll());
    }

    public LocationResponse getLocationById(Long id) {
        LocationEntity foundLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id: %s not fount".formatted(id)));
        return locationMapper.toResponse(foundLocation);
    }

    //@Transactional???
    public LocationResponse createLocation(LocationRequest request) {
        LocationEntity savedLocationEntity = locationRepository.save(locationMapper.toEntity(request));
        return locationMapper.toResponse(savedLocationEntity);
    }

    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id: %s not fount".formatted(id));
        }
        locationRepository.deleteById(id);
    }

    // @Transactional???
    public LocationResponse updateLocation(Long id, LocationRequest request) {
        LocationEntity locationEntityToUpdate = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id: %s not fount".formatted(id)));

        locationMapper.updateEntity(request, locationEntityToUpdate);

        return locationMapper.toResponse(locationRepository.save(locationEntityToUpdate));
    }
}
