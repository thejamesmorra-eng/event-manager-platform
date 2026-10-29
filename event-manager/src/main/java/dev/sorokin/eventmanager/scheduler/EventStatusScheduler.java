package dev.sorokin.eventmanager.scheduler;

import dev.sorokin.eventcommon.kafka.dto.ChangeItemDto;
import dev.sorokin.eventcommon.kafka.dto.EventChangeKafkaMessageDto;
import dev.sorokin.eventcommon.kafka.enums.EventType;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.model.EventStatus;
import dev.sorokin.eventmanager.repository.EventRepository;
import dev.sorokin.eventmanager.repository.RegistrationRepository;
import dev.sorokin.eventmanager.kafka.EventKafkaPublisherService;
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
    private final RegistrationRepository registrationRepository;
    private final EventKafkaPublisherService eventKafkaPublisherService;

    @Transactional
    @Scheduled(cron = "${scheduler.event-status.cron}")
    public void updateWaitStartStatuses() {
        List<EventEntity> entityList = eventRepository.findAllByStatusAndStartAtLessThanEqual(
                EventStatus.WAIT_START,
                LocalDateTime.now());

        for (EventEntity entity : entityList) {
            entity.setStatus(EventStatus.STARTED);
            publishEventStatusUpdate(entity, EventStatus.WAIT_START, EventStatus.STARTED);
        }
    }

    @Transactional
    @Scheduled(cron = "${scheduler.event-status.cron}")
    public void updateStartedStatuses() {
        List<EventEntity> entityList = eventRepository.findStartedToFinish(LocalDateTime.now());

        for (EventEntity entity : entityList) {
            entity.setStatus(EventStatus.FINISHED);
            publishEventStatusUpdate(entity, EventStatus.STARTED, EventStatus.FINISHED);
        }
    }

    private void publishEventStatusUpdate(EventEntity entity, EventStatus oldStatus, EventStatus newStatus) {
        eventKafkaPublisherService.publishEventChange(new EventChangeKafkaMessageDto(
                EventType.EVENT_UPDATED,
                entity.getId(),
                entity.getOwner().getId(),
                null,
                registrationRepository.findAllUsersIdByEventId(entity.getId()),
                List.of(new ChangeItemDto("status", oldStatus.name(), newStatus.name()))
        ));
    }
}
