package dev.sorokin.eventnotificator.kafka;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessageDto;
import dev.sorokin.eventnotificator.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventKafkaListenerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${kafka.topic.event-changes}")
    public void listenEventChange(ConsumerRecord<UUID, EventChangeKafkaMessageDto> message) {
        try {
            log.info("Received Kafka message: {}", message.value().messageId());
            notificationService.processKafkaMessage(message.value());
        } catch (Exception e) {
            log.error("Failed to process Kafka message", e);
        }
    }
}
