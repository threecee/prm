# --- !Ups

ALTER TABLE componenttypes add COLUMN part_of_power_station boolean;
update componenttypes set part_of_power_station = false;

ALTER TABLE componenttypes ALTER COLUMN part_of_power_station set not null ;



# --- !Downs

ALTER TABLE componenttypes  drop COLUMN part_of_power_station ;
