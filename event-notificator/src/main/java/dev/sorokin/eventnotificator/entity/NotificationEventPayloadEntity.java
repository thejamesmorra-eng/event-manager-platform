package dev.sorokin.eventnotificator.entity;

import dev.sorokin.eventcommon.kafka.enums.EventType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification_event_payloads")
// добавить Lombok
public class NotificationEventPayloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payloadId;

    @Column(name = "message_id", unique = true, nullable = false)
    private UUID messageId;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurred_at;

    @Column(name = "changed_by_id")
    private Long changedById;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;
}
