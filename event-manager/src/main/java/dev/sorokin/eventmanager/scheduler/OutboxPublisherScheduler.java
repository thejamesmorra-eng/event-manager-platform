package dev.sorokin.eventmanager.scheduler;

import dev.sorokin.eventmanager.entity.OutboxEventEntity;
import dev.sorokin.eventmanager.service.OutboxPublisherService;
import dev.sorokin.eventmanager.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisherScheduler {

    private final OutboxService outboxService;
    private final OutboxPublisherService outboxPublisherService;

    @Scheduled(cron = "${scheduler.outbox-publisher.cron}")
    public void publish() {
        List<OutboxEventEntity> entities = outboxService.getPendingEntities();
        for (OutboxEventEntity entity : entities) {
            outboxPublisherService.processSingleEvent(entity);
        }
    }
}
