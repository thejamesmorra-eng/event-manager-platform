-- liquibase formatted sql

-- changeset Ivan_Shibin:0006
-- comment: Добавление колонки cost в таблицу events

ALTER TABLE events
ADD COLUMN IF NOT EXISTS cost INT NOT NULL CHECK (cost > 0);

-- rollback ALTER TABLE events DROP COLUMN IF EXISTS cost;