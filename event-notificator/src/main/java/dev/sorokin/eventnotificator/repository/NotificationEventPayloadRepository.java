package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEventPayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface NotificationEventPayloadRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {
    boolean existsByMessageId(UUID messageId);

    @Modifying
    @Query("""
            DELETE FROM NotificationEventPayloadEntity p
            WHERE NOT EXISTS (
                SELECT 1 FROM NotificationEntity n WHERE n.payload = p
            ) AND p.occurredAt < :cutoffDate
          """)
    int deleteOrphanPayloads(@Param("cutoffDate") Instant cutoffDate);
}
