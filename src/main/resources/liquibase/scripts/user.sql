-- liquibase formatted sql

-- changeset ilia:5
CREATE TABLE  notification_task(
    id serial primary key,
    chat_id numeric,
    text varchar(250),
    local_date_time timestamp
    );

-- changeset ilia:6
CREATE INDEX local_date_time_idx ON notification_task (local_date_time);



