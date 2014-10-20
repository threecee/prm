# --- !Ups

CREATE TABLE downtimecosts (
  id        serial primary key,
  span     int not null,
  cost     double precision not null,
  powerunit int references powerunits(id) not null
);

# --- !Downs

DROP TABLE IF EXISTS downtimecosts;