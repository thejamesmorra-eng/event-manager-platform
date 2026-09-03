package dev.sorokin.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter
@Setter
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @PrePersist
    @PreUpdate
    private void validateBeforeSave() {
        if (name == null || name.isBlank() ||
                address == null || address.isBlank() ||
                capacity == null || capacity <= 0 ||
                description == null || description.isBlank()) {
            throw new IllegalStateException("All fields must be valid");
        }
    }
}
