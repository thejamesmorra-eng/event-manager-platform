-- liquibase formatted sql

-- changeset Ivan_Shibin:0002
-- comment: Создание таблицы notifications

CREATE TABLE notifications (
    notification_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    payload_id BIGINT NOT NULL REFERENCES notification_event_payloads(payload_id),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    read_at TIMESTAMP WITH TIME ZONE
);

-- rollback DROP TABLE IF EXISTS notifications CASCADE;