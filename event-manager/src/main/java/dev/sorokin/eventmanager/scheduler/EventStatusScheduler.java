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

    private static final long RATE = 60_000;

    private final EventRepository eventRepository;

    @Transactional
    @Scheduled(fixedRate = RATE)
    public void updateStartedStatuses() {
        List<EventEntity> entityList = eventRepository.findAllByStatus(EventStatus.STARTED);

        LocalDateTime now = LocalDateTime.now();
        for (EventEntity entity : entityList) {
            if (!entity.getStartAt().plusMinutes(entity.getDurationMinutes()).isAfter(now)) {
                entity.setStatus(EventStatus.FINISHED);
            }
        }
    }

    @Transactional
    @Scheduled(fixedRate = RATE)
    public void updateWaitStartStatuses() {
        List<EventEntity> entityList = eventRepository.findAllByStatus(EventStatus.WAIT_START);

        LocalDateTime now = LocalDateTime.now();
        for (EventEntity entity : entityList) {
            if (!entity.getStartAt().isAfter(now)) {
                entity.setStatus(EventStatus.STARTED);
            }
        }
    }
}
