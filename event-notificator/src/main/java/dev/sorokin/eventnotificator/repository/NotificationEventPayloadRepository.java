package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEventPayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationEventPayloadRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {
    boolean existsByMessageId(UUID messageId);
}
