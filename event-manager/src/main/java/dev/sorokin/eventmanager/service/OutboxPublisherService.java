package dev.sorokin.eventmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.kafka.dto.EventChangeKafkaMessageDto;
import dev.sorokin.eventmanager.entity.OutboxEventEntity;
import dev.sorokin.eventmanager.exception.OutboxPublishException;
import dev.sorokin.eventmanager.kafka.EventKafkaPublisherService;
import dev.sorokin.eventmanager.model.OutboxStatus;
import dev.sorokin.eventmanager.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherService {

    private final EventKafkaPublisherService eventKafkaPublisherService;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Value("${outbox.publisher.send-timeout-ms}")
    private long sendTimeoutMs;

    public void processSingleEvent(OutboxEventEntity entity) {
        EventChangeKafkaMessageDto message;
        try {
            message = objectMapper.readValue(entity.getPayload(), EventChangeKafkaMessageDto.class);
        } catch (JsonProcessingException e) {
            log.error("Outbox event {} has invalid payload", entity.getId(), e);
            entity.setStatus(OutboxStatus.FAILED);
            outboxEventRepository.save(entity);
            return;
        }

        try {
            eventKafkaPublisherService.publishEventChange(message).get(sendTimeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OutboxPublishException("Interrupted while publishing " + entity.getId(), e);
        } catch (ExecutionException | TimeoutException | KafkaException e) {
            throw new OutboxPublishException("Failed to publish " + entity.getId(), e);
        }

        entity.setStatus(OutboxStatus.PUBLISHED);
        entity.setPublishedAt(Instant.now());
        outboxEventRepository.save(entity);
    }
}
