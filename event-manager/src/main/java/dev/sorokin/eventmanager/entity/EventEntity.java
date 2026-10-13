package dev.sorokin.eventmanager.entity;

import dev.sorokin.eventmanager.model.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;

    @Column(name = "occupied_places", nullable = false)
    private Integer occupiedPlaces;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private LocationEntity location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private UserEntity owner;

    public EventEntity(String name,
                       LocalDateTime startAt,
                       Integer durationMinutes,
                       Integer maxPlaces,
                       Integer occupiedPlaces,
                       Integer cost,
                       EventStatus status,
                       LocationEntity location,
                       UserEntity owner) {
        this.name = name;
        this.startAt = startAt;
        this.durationMinutes = durationMinutes;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.cost = cost;
        this.status = status;
        this.location = location;
        this.owner = owner;
    }

    @PrePersist
    @PreUpdate
    private void validateBeforeSave() {
        if (name == null || name.isBlank() ||
                startAt == null ||
                durationMinutes == null || durationMinutes < 30 ||
                maxPlaces == null || maxPlaces <= 0 ||
                occupiedPlaces == null || occupiedPlaces < 0 ||
                cost == null || cost <= 0 ||
                status == null ||
                location == null ||
                owner == null) {
            throw new IllegalStateException("All fields must be valid");
        }
    }
}
