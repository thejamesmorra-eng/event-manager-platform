package dev.sorokin.eventmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.kafka.dto.EventChangeKafkaMessageDto;
import dev.sorokin.eventmanager.entity.OutboxEventEntity;
import dev.sorokin.eventmanager.exception.OutboxSerializationException;
import dev.sorokin.eventmanager.model.OutboxStatus;
import dev.sorokin.eventmanager.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveToOutbox(EventChangeKafkaMessageDto message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            OutboxEventEntity outboxEventEntity = new OutboxEventEntity(message.messageId(), payload);
            outboxEventRepository.save(outboxEventEntity);
        } catch (JsonProcessingException e) {
            throw new OutboxSerializationException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<OutboxEventEntity> getPendingEntities() {
        return outboxEventRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);
    }
}
