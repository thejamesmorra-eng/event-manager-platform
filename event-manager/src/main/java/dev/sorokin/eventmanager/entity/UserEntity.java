package dev.sorokin.eventmanager.entity;

import dev.sorokin.eventmanager.model.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserEntity(String login, Integer age, String passwordHash, UserRole role) {
        this.login = login;
        this.age = age;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    @PrePersist
    @PreUpdate
    private void validateBeforeSave() {
        if (login == null || login.isBlank() ||
                passwordHash == null || passwordHash.isBlank() ||
                age == null || age <= 0 ||
                role == null) {
            throw new IllegalStateException("All fields must be valid");
        }
    }
}
