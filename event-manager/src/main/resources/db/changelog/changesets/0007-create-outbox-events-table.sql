-- liquibase formatted sql

-- changeset Ivan_Shibin:0007
-- comment: Создание таблицы outbox_events

CREATE TABLE outbox_events (
    id BIGSERIAL PRIMARY KEY,
    message_id UUID UNIQUE NOT NULL,
    payload JSONB NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE
);

-- rollback DROP TABLE IF EXISTS outbox_events CASCADE;