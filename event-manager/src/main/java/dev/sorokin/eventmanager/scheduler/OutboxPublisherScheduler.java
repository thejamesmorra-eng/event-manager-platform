package dev.sorokin.eventmanager.scheduler;

import dev.sorokin.eventmanager.entity.OutboxEventEntity;
import dev.sorokin.eventmanager.exception.OutboxPublishException;
import dev.sorokin.eventmanager.service.OutboxPublisherService;
import dev.sorokin.eventmanager.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherScheduler {

    private final OutboxService outboxService;
    private final OutboxPublisherService outboxPublisherService;

    @Scheduled(cron = "${scheduler.outbox-publisher.cron}")
    public void publish() {
        List<OutboxEventEntity> entities = outboxService.getPendingEntities();
        for (OutboxEventEntity entity : entities) {
            try {
                outboxPublisherService.processSingleEvent(entity);
            } catch (OutboxPublishException e) {
                log.warn("Kafka unavailable, stop batch and retry later: {}", e.getMessage(), e.getCause());
                break;
            } catch (RuntimeException e) {
                log.error("Unexpected error while processing outbox event {}", entity.getId(), e);
                break;
            }
        }
    }
}
