package dev.sorokin.eventnotificator.scheduler;

import dev.sorokin.eventnotificator.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationCleanupScheduler {

    private final NotificationRepository notificationRepository;

    @Value("${scheduler.notification-cleanup.retention-days}")
    private long retentionDays;

    @Transactional
    @Scheduled(cron = "${scheduler.notification-cleanup.cron}")
    public void cleanupOldNotifications() {
        Instant cutoffDate = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
        notificationRepository.deleteByCreatedAtBefore(cutoffDate);
        log.info("Deleted notifications older than {} days (before {})", retentionDays, cutoffDate);
    }
}