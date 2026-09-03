package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.request.LocationRequest;
import dev.sorokin.eventmanager.dto.response.LocationResponse;
import dev.sorokin.eventmanager.entity.LocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    // ==================Entity -> DTO==================
    LocationResponse toResponse(LocationEntity locationEntity);

    // ==================DTO -> Entity==================
    // Create
    @Mapping(target = "id", ignore = true)
    LocationEntity toEntity(LocationRequest request);

    // Update
    @Mapping(target = "id", ignore = true)
    void updateEntity(LocationRequest request, @MappingTarget LocationEntity entity);
}
