-- liquibase formatted sql

-- changeset Ivan_Shibin:0003
-- comment: Добавление колонки event_name в таблицу notification_event_payloads

ALTER TABLE notification_event_payloads
ADD COLUMN IF NOT EXISTS event_name VARCHAR(255) NOT NULL;

-- rollback ALTER TABLE notification_event_payloads DROP COLUMN IF EXISTS event_name;