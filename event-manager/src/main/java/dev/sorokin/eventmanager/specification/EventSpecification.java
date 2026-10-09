package dev.sorokin.eventmanager.specification;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.model.EventStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventSpecification {

    private EventSpecification() {}

    public static Specification<EventEntity> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("name"), name);
        };
    }

    public static Specification<EventEntity> placesGreaterThanOrEqual(Integer placesMin) {
        return (root, query, cb) -> {
            if (placesMin == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("maxPlaces"), placesMin);
        };
    }

    public static Specification<EventEntity> placesLessThanOrEqual(Integer placesMax) {
        return (root, query, cb) -> {
            if (placesMax == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("maxPlaces"), placesMax);
        };
    }

    public static Specification<EventEntity> startAtAfter(LocalDateTime dateStartAfter) {
        return (root, query, cb) -> {
            if (dateStartAfter == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("startAt"), dateStartAfter);
        };
    }

    public static Specification<EventEntity> startAtBefore(LocalDateTime dateStartBefore) {
        return (root, query, cb) -> {
            if (dateStartBefore == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("startAt"), dateStartBefore);
        };
    }

    public static Specification<EventEntity> costGreaterThanOrEqual(Integer costMin) {
        return (root, query, cb) -> {
            if (costMin == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("cost"), costMin);
        };
    }

    public static Specification<EventEntity> costLessThanOrEqual(Integer costMax) {
        return (root, query, cb) -> {
            if (costMax == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("cost"), costMax);
        };
    }

    public static Specification<EventEntity> durationGreaterThanOrEqual(Integer durationMin) {
        return (root, query, cb) -> {
            if (durationMin == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("durationMinutes"), durationMin);
        };
    }

    public static Specification<EventEntity> durationLessThanOrEqual(Integer durationMax) {
        return (root, query, cb) -> {
            if (durationMax == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("durationMinutes"), durationMax);
        };
    }

    public static Specification<EventEntity> hasLocationId(Long locationId) {
        return (root, query, cb) -> {
            if (locationId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("location").get("id"), locationId);
        };
    }

    public static Specification<EventEntity> hasStatus(EventStatus status) {
        return (root, query, cb) -> {
          if (status == null) {
              return cb.conjunction();
          }
          return cb.equal(root.get("status"), status);
        };
    }
}
