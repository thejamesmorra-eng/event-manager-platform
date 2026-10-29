package dev.sorokin.eventnotificator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessageDto;
import dev.sorokin.eventnotificator.entity.NotificationEntity;
import dev.sorokin.eventnotificator.entity.NotificationEventPayloadEntity;
import dev.sorokin.eventnotificator.repository.NotificationEventPayloadRepository;
import dev.sorokin.eventnotificator.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationEventPayloadRepository payloadRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processKafkaMessage(EventChangeKafkaMessageDto message) {

        if (payloadRepository.existsByMessageId(message.messageId())) {
            log.info("Duplicate message skipped: {}", message.messageId());
            return;
        }

        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(message.changes());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize changes", e);
            throw new RuntimeException("Failed to serialize payload", e);
        }

        NotificationEventPayloadEntity payloadEntity = new NotificationEventPayloadEntity();
        payloadEntity.setMessageId(message.messageId());
        payloadEntity.setEventType(message.eventType());
        payloadEntity.setEventId(message.eventId());
        payloadEntity.setOccurredAt(message.occurredAt());
        payloadEntity.setChangedById(message.changedById());
        payloadEntity.setOwnerId(message.ownerId());
        payloadEntity.setPayload(jsonPayload);

        payloadEntity = payloadRepository.save(payloadEntity);

        List<NotificationEntity> notifications = new ArrayList<>();
        for (Long userId : message.subscribers()) {
            NotificationEntity notificationEntity = new NotificationEntity(
                    userId,
                    payloadEntity
            );
            notifications.add(notificationEntity);
        }
        notificationRepository.saveAll(notifications);
    }
}
