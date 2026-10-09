package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.model.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public void checkPermissionOrThrow(EventEntity eventEntity, String ownerLogin, UserRole role) {
        if (!eventEntity.getOwner().getLogin().equals(ownerLogin) && !(role == UserRole.ADMIN)) {
            throw new AccessDeniedException("Access denied");
        }
    }
}