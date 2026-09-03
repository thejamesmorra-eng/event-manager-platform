-- liquibase formatted sql

-- changeset Ivan_Shibin:0001
-- comment: Создание таблицы locations

CREATE TABLE locations (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        address VARCHAR(255) NOT NULL,
        capacity INTEGER CHECK (capacity > 0)
);

-- rollback DROP TABLE IF EXISTS locations CASCADE;