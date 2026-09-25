package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long>,
                                            JpaSpecificationExecutor<EventEntity> {
    List<EventEntity> findAllByOwnerId(Long ownerId);
    boolean existsByLocationId(Long locationId);
}
