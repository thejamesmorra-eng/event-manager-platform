package dev.sorokin.eventmanager.entity;

import dev.sorokin.eventmanager.model.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @PrePersist
    @PreUpdate
    private void validateBeforeSave() {
        if (login == null || login.isBlank() ||
                passwordHash == null || passwordHash.isBlank() ||
                role == null) {
            throw new IllegalStateException("All fields must be valid");
        }
    }
}
