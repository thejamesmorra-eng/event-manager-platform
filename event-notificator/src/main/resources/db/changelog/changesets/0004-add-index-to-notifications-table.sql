-- liquibase formatted sql

-- changeset Ivan_Shibin:0004
-- comment: Добавление индексов на payload_id и created_at в таблицу notifications

CREATE INDEX idx_notifications_payload_id ON notifications(payload_id);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);

-- rollback DROP INDEX IF EXISTS idx_notifications_payload_id;
-- rollback DROP INDEX IF EXISTS idx_notifications_created_at;