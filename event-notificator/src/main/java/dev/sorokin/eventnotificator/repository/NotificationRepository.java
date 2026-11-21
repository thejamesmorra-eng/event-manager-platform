package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @EntityGraph(attributePaths = "payload")
    List<NotificationEntity> findAllByUserIdAndIsReadIsFalse(Long userId);
    List<NotificationEntity> findAllByNotificationIdInAndUserIdAndIsReadIsFalse(Collection<Long> notificationIds, Long userId);

    @Modifying
    @Query("DELETE FROM NotificationEntity e WHERE e.createdAt < :cutoffDate")
    int deleteByCreatedAtBefore(@Param("cutoffDate") Instant cutoffDate);
}
