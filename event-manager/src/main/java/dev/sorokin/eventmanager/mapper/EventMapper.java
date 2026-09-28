package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.response.EventDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    // ==================Entity -> DTO==================
    @Mapping(source = "startAt", target = "date")
    @Mapping(source = "durationMinutes", target = "duration")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "owner.id", target = "ownerId")
    EventDto toResponse(EventEntity eventEntity);

    List<EventDto> toResponseList(List<EventEntity> eventEntities);
}
