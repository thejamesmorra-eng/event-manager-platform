package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    Optional<RegistrationEntity> findByEventIdAndUserId(Long eventId, Long userId);

    @EntityGraph(attributePaths = "event")
    List<RegistrationEntity> findAllByUserId(Long userId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    @Query("SELECT r.user.id FROM RegistrationEntity r WHERE r.event.id = :eventId")
    List<Long> findAllUsersIdByEventId(@Param("eventId") Long eventId);
}
