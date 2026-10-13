package dev.sorokin.eventmanager.scheduler;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.model.EventStatus;
import dev.sorokin.eventmanager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventStatusScheduler {

    private final EventRepository eventRepository;

    @Transactional
    @Scheduled(cron = "${scheduler.event-status.cron}")
    public void updateWaitStartStatuses() {
        List<EventEntity> entityList = eventRepository.findAllByStatusAndStartAtLessThanEqual(
                EventStatus.WAIT_START,
                LocalDateTime.now());

        for (EventEntity entity : entityList) {
            entity.setStatus(EventStatus.STARTED);
        }
    }

    @Transactional
    @Scheduled(cron = "${scheduler.event-status.cron}")
    public void updateStartedStatuses() {
        List<EventEntity> entityList = eventRepository.findStartedToFinish(LocalDateTime.now());

        for (EventEntity entity : entityList) {
            entity.setStatus(EventStatus.FINISHED);
        }
    }
}
