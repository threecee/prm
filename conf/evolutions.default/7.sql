# --- !Ups

CREATE TABLE powerunits (
  id        serial primary key,
  powerstation int references powerstations(id) not null
);

# --- !Downs

DROP TABLE IF EXISTS powerunits;