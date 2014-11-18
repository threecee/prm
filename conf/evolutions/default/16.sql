# --- !Ups

CREATE TABLE regions (
  id        serial primary key,
  name     varchar(50) not null
);

# --- !Downs

DROP TABLE IF EXISTS region;