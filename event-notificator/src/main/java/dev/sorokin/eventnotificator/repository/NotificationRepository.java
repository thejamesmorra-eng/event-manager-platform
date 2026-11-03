package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByUserIdAndIsReadIsFalse(Long userId);
    List<NotificationEntity> findAllByNotificationIdInAndUserIdAndIsReadIsFalse(Collection<Long> notificationIds, Long userId);
}
