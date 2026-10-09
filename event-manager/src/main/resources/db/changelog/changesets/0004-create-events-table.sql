-- liquibase formatted sql

-- changeset Ivan_Shibin:0004
-- comment: Создание таблицы events

CREATE TABLE events (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        start_at TIMESTAMP NOT NULL,
        duration_minutes INT NOT NULL CHECK (duration_minutes >= 30),
        max_places INT NOT NULL CHECK (max_places > 0),
        occupied_places INT NOT NULL DEFAULT 0 CHECK (occupied_places >= 0),
        status VARCHAR(50) NOT NULL,
        location_id BIGINT NOT NULL REFERENCES locations(id),
        owner_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE INDEX idx_events_location_id ON events(location_id);
CREATE INDEX idx_events_owner_id ON events(owner_id);
CREATE INDEX idx_events_status ON events(status);
CREATE INDEX idx_events_start_at ON events(start_at);

-- rollback DROP TABLE IF EXISTS events CASCADE;