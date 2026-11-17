package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.OutboxEventEntity;
import dev.sorokin.eventmanager.model.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, Long> {
    List<OutboxEventEntity> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
