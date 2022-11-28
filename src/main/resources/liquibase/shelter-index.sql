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
