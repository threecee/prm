# --- !Ups

CREATE TABLE powerstations (
  id        serial primary key,
  name varchar(100) not null
);

# --- !Downs

DROP TABLE IF EXISTS powerstations;