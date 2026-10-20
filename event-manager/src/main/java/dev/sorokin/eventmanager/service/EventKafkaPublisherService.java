package dev.sorokin.eventmanager.service;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventKafkaPublisherService {

    private final KafkaTemplate<UUID, EventChangeKafkaMessageDto> kafkaTemplate;

    @Value("${kafka.topic.event-changes}")
    private String topic;

    public void publishEventChange(EventChangeKafkaMessageDto message) {
        log.debug("Publish event changes: {}", message);
        CompletableFuture<SendResult<UUID, EventChangeKafkaMessageDto>> result = kafkaTemplate.send(topic, message.messageId(), message);
        result.whenComplete((success, ex) -> {
            if (ex != null) {
                log.error("Failed to publish event to kafka: {}", message.messageId(), ex);
            } else {
                log.info("Event publishes successfully: {}", message.messageId());
            }
        });
    }
}
