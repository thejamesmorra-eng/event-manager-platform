package dev.sorokin.eventnotificator.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventnotificator.dto.NotificationChange;
import dev.sorokin.eventnotificator.dto.NotificationPayload;
import dev.sorokin.eventnotificator.dto.response.NotificationResponse;
import dev.sorokin.eventnotificator.entity.NotificationEntity;
import dev.sorokin.eventnotificator.entity.NotificationEventPayloadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(source = "payload.eventType", target = "type")
    @Mapping(source = "payload.eventId", target = "eventId")
    @Mapping(target = "message", constant = "Событие было изменено")
    @Mapping(source = "payload", target = "payload")
    public abstract NotificationResponse toResponse(NotificationEntity entity);
    public abstract List<NotificationResponse> toResponseList(List<NotificationEntity> entities);

    protected NotificationPayload toPayload(NotificationEventPayloadEntity payloadEntity) {
        if (payloadEntity == null) {
            return null;
        }

        try {
            List<NotificationChange> changes = objectMapper.readValue(
                    payloadEntity.getPayload(),
                    new TypeReference<List<NotificationChange>>() {}
            );

            return new NotificationPayload(
                    payloadEntity.getMessageId(),
                    payloadEntity.getEventType(),
                    payloadEntity.getOccurredAt(),
                    payloadEntity.getChangedById(),
                    payloadEntity.getOwnerId(),
                    payloadEntity.getEventName(),
                    changes
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse payload JSON", e);
        }
    }
}
