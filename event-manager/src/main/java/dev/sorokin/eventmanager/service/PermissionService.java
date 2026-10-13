package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.model.UserRole;
import dev.sorokin.eventmanager.security.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final SecurityContextService securityContextService;

    public void checkCanModify(EventEntity eventEntity) {
        String currentLogin = securityContextService.getCurrentLogin();
        UserRole currentRole = securityContextService.getCurrentRole();

        boolean isOwner = eventEntity.getOwner().getLogin().equals(currentLogin);
        boolean isAdmin = currentRole == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Access denied");
        }
    }
}