# --- !Ups

CREATE TABLE componenttypes (
  id                      serial primary key,
  name                varchar(100) not null
);

# --- !Downs

DROP TABLE IF EXISTS componenttypes;