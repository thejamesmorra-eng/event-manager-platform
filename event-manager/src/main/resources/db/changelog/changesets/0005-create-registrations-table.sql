-- liquibase formatted sql

-- changeset Ivan_Shibin:0005
-- comment: Создание таблицы registrations

CREATE TABLE registrations (
        id BIGSERIAL PRIMARY KEY,
        event_id BIGINT NOT NULL REFERENCES events(id),
        user_id BIGINT NOT NULL REFERENCES users(id),
        created_at TIMESTAMP NOT NULL,
        CONSTRAINT uk_registrations_event_user UNIQUE (event_id, user_id)
);

CREATE INDEX idx_registrations_user_id ON registrations(user_id);

-- rollback DROP TABLE IF EXISTS registrations CASCADE;