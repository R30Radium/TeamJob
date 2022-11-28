-- liquibase formatted sql

-- changeset shulga:1
CREATE TABLE shelter
(
    id serial PRIMARY KEY,
    information_about_shelter TEXT,
    work_schedule_shelter TEXT,
    address_shelter TEXT,
    driving_directions_shelter TEXT,
    safety_at_shelter TEXT
);

-- changeset shulga:2
CREATE INDEX IF NOT EXISTS id_index ON shelter (id);

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