-- liquibase formatted sql

-- changeset Ivan_Shibin:0001
-- comment: Создание таблицы notification_event_payloads

CREATE TABLE notification_event_payloads (
    payload_id BIGSERIAL PRIMARY KEY,
    message_id UUID UNIQUE NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_id BIGINT NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    changed_by_id BIGINT,
    owner_id BIGINT NOT NULL,
    payload jsonb NOT NULL
);

-- rollback DROP TABLE IF EXISTS notification_event_payloads CASCADE;