# --- !Ups

CREATE TABLE components (
  id        serial primary key,
  componenttype int references componenttypes(id) not null,
  eqipmentstate int references eqipmentstates(id) not null,
  powerunit int references  powerunits(id) not null
);

# --- !Downs

DROP TABLE IF EXISTS components;