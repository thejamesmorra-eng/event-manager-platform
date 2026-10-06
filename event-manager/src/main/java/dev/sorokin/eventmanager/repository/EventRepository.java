package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.model.EventStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {
    List<EventEntity> findAllByOwnerId(Long ownerId);
    boolean existsByLocationId(Long locationId);
    List<EventEntity> findAllByLocationIdAndStatusIn(Long locationId, Collection<EventStatus> statuses);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
    Optional<EventEntity> getEventByIdForUpdate(@Param("id") Long id);
}
