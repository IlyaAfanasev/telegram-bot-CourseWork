-- liquibase formatted sql

-- changeset ilia:5
CREATE TABLE  notification_task(
    id serial primary key,
    chat_id numeric,
    text varchar(250),
    local_date_time timestamp
    );

