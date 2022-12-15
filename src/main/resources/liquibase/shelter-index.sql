-- liquibase formatted sql

-- changeset author:1
CREATE TABLE shelter
(
    id serial PRIMARY KEY,
    information_about_shelter TEXT,
    work_schedule_shelter TEXT,
    address_shelter TEXT,
    driving_directions_shelter TEXT,
    safety_at_shelter TEXT


);

-- changeset author:2
CREATE INDEX IF NOT EXISTS id_index ON shelter (id);



-- changeset author:3
CREATE TABLE contact
(
    id serial PRIMARY KEY,
    name TEXT,
    number_phone TEXT,
    date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL

);


-- changeset author:4
CREATE TABLE information_for_owner
(
    id serial PRIMARY KEY,
    rules TEXT,
    docs TEXT,
    transpartation TEXT,
    arrangement_puppy TEXT,
    arrangement_dog TEXT,
    arrangement_dog_invalid TEXT,
    cynologist TEXT,
    good_cynologists TEXT,
    reject TEXT


);
-- changeset author:5
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
    chat_id BIGSERIAL NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES users (chat_Id),
    date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    diet TEXT      NOT NULL,
    adaptation TEXT      NOT NULL,
    change_in_behavior TEXT      NOT NULL
);

CREATE TABLE petPhotos
(
    pet_Photos_id BIGSERIAL NOT NULL PRIMARY KEY,
    file_path   TEXT      NOT NULL,
    file_size   BIGINT    NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence;










