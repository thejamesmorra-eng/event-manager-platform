package dev.sorokin.eventmanager.service;

import dev.sorokin.eventcommon.kafka.ChangeItemDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class EventChangeTracker {

    private final List<ChangeItemDto> changes = new ArrayList<>();

    void trackChanges(String field, Object oldValue, Object newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            changes.add(new ChangeItemDto(
                    field,
                    oldValue != null ? oldValue.toString() : null,
                    newValue != null ? newValue.toString() : null
            ));
        }
    }

    boolean hasChanges() {
        return !changes.isEmpty();
    }

    List<ChangeItemDto> getChanges() {
        return changes;
    }
}
