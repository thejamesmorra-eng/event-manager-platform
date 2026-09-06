-- liquibase formatted sql

-- changeset Ivan_Shibin:0003
-- comment: Создание таблицы users

CREATE TABLE users (
        id BIGSERIAL PRIMARY KEY,
        login VARCHAR(255) UNIQUE NOT NULL,
        password_hash VARCHAR(255) NOT NULL,
        role VARCHAR(50) NOT NULL
);

-- rollback DROP TABLE IF EXISTS users CASCADE;