# --- !Ups

CREATE TABLE repairs (
  id                   serial primary key,
  span                 double precision not null,
  cost                 double precision not null,
  incidenttype         int references incidenttypes(id) not null,
  probability          int not null,
  componenttype        int references componenttypes(id) not null
);

# --- !Downs

DROP TABLE IF EXISTS repairs;