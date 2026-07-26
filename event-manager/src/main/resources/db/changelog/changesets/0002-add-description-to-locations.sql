-- liquibase formatted SQL

-- changeset Ivan_Shibin:0002
-- comment: Добавление колонки description в таблицу locations

ALTER TABLE locations
ADD COLUMN IF NOT EXISTS description VARCHAR(500) NOT NULL;

-- rollback ALTER TABLE locations DROP COLUMN IF EXISTS description;