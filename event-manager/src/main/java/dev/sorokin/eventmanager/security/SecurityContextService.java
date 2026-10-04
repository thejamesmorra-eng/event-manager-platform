package dev.sorokin.eventmanager.security;

import dev.sorokin.eventmanager.model.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    public String getCurrentLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public UserRole getCurrentRole() {
        return UserRole.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .iterator().next().getAuthority().replace(SecurityConstants.ROLE_PREFIX, ""));
    }
}
