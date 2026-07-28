package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.request.LocationRequest;
import dev.sorokin.eventmanager.dto.response.LocationResponse;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.exception.LocationAlreadyExistsException;
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
    // @Transactoinal(readOnly = true)???
    public List<LocationResponse> getAllLocations() {
        return locationMapper.toResponseList(locationRepository.findAll());
    }

    public LocationResponse getLocationById(Long id) {
        LocationEntity foundLocationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id: %s not found".formatted(id)));
        return locationMapper.toResponse(foundLocationEntity);
    }

    //@Transactional???
    public LocationResponse createLocation(LocationRequest request) {
        // TO DO
        // - Проверить нет ли события с таким же названием (добавить кастомный метод в репозиторий)
        // - Подумать, что если даже названия событий одинаковые, но разные адреса - то допустимо
        if (locationRepository.existsLocationEntityByNameAndAddress(request.name(), request.address())) {
            throw new LocationAlreadyExistsException("Location: %s with address: %s is already exists".formatted(request.name(), request.address()));
        }

        LocationEntity savedLocationEntity = locationRepository.save(locationMapper.toEntity(request));
        return locationMapper.toResponse(savedLocationEntity);
    }

    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id: %s not found".formatted(id));
        }
        locationRepository.deleteById(id);
    }

    // @Transactional???
    public LocationResponse updateLocation(Long id, LocationRequest request) {
        LocationEntity locationEntityToUpdate = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id: %s not found".formatted(id)));

        // TO DO
        // - Проверить нет ли события с таким же названием (добавить кастомный метод в репозиторий)
        // - Подумать, что если даже названия событий одинаковые, но разные адреса - то допустимо
        // - Тут такое работать не будет - мы обновляем текущую локацию и если, например хотим изменить
        // - только capacity, то будет ошибка, а это неправильно - подумать...
        locationMapper.updateEntity(request, locationEntityToUpdate);

        return locationMapper.toResponse(locationRepository.save(locationEntityToUpdate));
    }
}
