# --- !Ups

CREATE TABLE incidenttypes (
  id                      serial primary key,
  name                varchar(100) not null,
  description         varchar(1000) not null
);

# --- !Downs

DROP TABLE IF EXISTS incidenttypes;