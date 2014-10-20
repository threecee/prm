# --- !Ups

CREATE TABLE residuallifespans (
  id                      serial primary key,
  equipmentstate int references equipmentstates(id) not null,
  span                double precision not null,
  componenttype int references componenttypes(id)
);

# --- !Downs

DROP TABLE IF EXISTS residuallifespans;