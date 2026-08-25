package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.request.LocationRequest;
import dev.sorokin.eventmanager.dto.response.LocationResponse;
import dev.sorokin.eventmanager.dto.response.PageResponse;
import dev.sorokin.eventmanager.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<PageResponse<LocationResponse>> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        log.debug("Get all locations");
        return ResponseEntity.ok(locationService.getAllLocations(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getById(@PathVariable Long id) {
        log.debug("Get location by id: {}", id);
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @PostMapping
    public ResponseEntity<LocationResponse> create(@Valid @RequestBody LocationRequest request) {
        log.debug("Create location: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> update(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        log.debug("Update location: {} with id: {}", request, id);
        return ResponseEntity.ok(locationService.updateLocation(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Delete location with id: {}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
