package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByLogin(String login);
}
