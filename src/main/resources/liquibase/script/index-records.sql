-- liquibase formatted sql

-- changeset LeonidB:1
CREATE TABLE users
(
    user_id      BIGSERIAL NOT NULL PRIMARY KEY,
    chat_Id      BIGSERIAL UNIQUE NOT NULL,
    user_name    TEXT      NOT NULL,
    number_phone TEXT      NOT NULL,
    pet_name     TEXT
);

CREATE TABLE records
(
    record_id    BIGSERIAL NOT NULL PRIMARY KEY,
    user_id     BIGSERIAL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    chat_id BIGSERIAL NOT NULL,
    date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    life_record TEXT      NOT NULL
);

CREATE TABLE petPhotos
(
    pet_Photos_id BIGSERIAL NOT NULL PRIMARY KEY,
    record_id   BIGSERIAL,
    FOREIGN KEY (record_id) REFERENCES records (record_id),
    file_path   TEXT      NOT NULL,
    file_size   BIGINT    NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence;