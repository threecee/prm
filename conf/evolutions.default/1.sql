# --- First database schema

# --- !Ups

CREATE TABLE equipmentstates (
  id                      serial primary key,
  value                   int not null
);

# --- !Downs

DROP TABLE IF EXISTS equipmentstates;