-- liquibase formatted sql

-- changeset author:6
CREATE TABLE client
(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGSERIAL,
    status INT
);