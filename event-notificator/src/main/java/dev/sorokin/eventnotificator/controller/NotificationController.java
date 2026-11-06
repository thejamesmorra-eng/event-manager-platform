package dev.sorokin.eventnotificator.controller;

import dev.sorokin.eventnotificator.dto.request.MarkNotificationsAsReadRequest;
import dev.sorokin.eventnotificator.dto.response.NotificationResponse;
import dev.sorokin.eventnotificator.security.SecurityContextService;
import dev.sorokin.eventnotificator.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final SecurityContextService securityContextService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        Long userId = securityContextService.getCurrentUserId();
        log.debug("Get unread notifications for user id: {}", userId);
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @PostMapping
    public ResponseEntity<Void> markAsRead(@Valid @RequestBody MarkNotificationsAsReadRequest request) {
        Long userId = securityContextService.getCurrentUserId();
        log.debug("Mark notifications: {} as read for user id: {}", request.notificationIds(), userId);
        notificationService.markAsRead(userId, request.notificationIds());
        return ResponseEntity.noContent().build();
    }
}
