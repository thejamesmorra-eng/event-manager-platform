package dev.sorokin.eventmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.kafka.dto.EventChangeKafkaMessageDto;
import dev.sorokin.eventmanager.entity.OutboxEventEntity;
import dev.sorokin.eventmanager.exception.OutboxDeserializationException;
import dev.sorokin.eventmanager.kafka.EventKafkaPublisherService;
import dev.sorokin.eventmanager.model.OutboxStatus;
import dev.sorokin.eventmanager.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxPublisherService {

    private final EventKafkaPublisherService eventKafkaPublisherService;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processSingleEvent(OutboxEventEntity entity) {
        try {
            EventChangeKafkaMessageDto message = objectMapper.readValue(entity.getPayload(), EventChangeKafkaMessageDto.class);
            eventKafkaPublisherService.publishEventChange(message);

            entity.setStatus(OutboxStatus.PUBLISHED);
            entity.setPublishedAt(Instant.now());

            outboxEventRepository.save(entity);
        } catch (JsonProcessingException e) {
            throw new OutboxDeserializationException(e.getMessage());
        }

    }
}
