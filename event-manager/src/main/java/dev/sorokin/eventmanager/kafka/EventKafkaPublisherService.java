package dev.sorokin.eventmanager.kafka;

import dev.sorokin.eventcommon.kafka.dto.EventChangeKafkaMessageDto;
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

    public CompletableFuture<SendResult<UUID, EventChangeKafkaMessageDto>> publishEventChange(
            EventChangeKafkaMessageDto message) {
        log.debug("Publish event changes: {}", message);
        return kafkaTemplate.send(topic, message.messageId(), message);
    }
}
