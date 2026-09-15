package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.response.UserResponse;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ==================Entity -> DTO==================
    UserResponse toResponse(UserEntity userEntity);
}
