package dev.sorokin.eventnotificator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "read_at")
    private Instant readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payload_id", nullable = false)
    private NotificationEventPayloadEntity payload;

    public NotificationEntity(
            Long userId,
            NotificationEventPayloadEntity payload) {
        this.userId = userId;
        this.isRead = false;
        this.createdAt = Instant.now();
        this.readAt = null;
        this.payload = payload;
    }
}
