package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    boolean existsLocationEntityByNameAndAddress(String name, String address);
}
