# --- !Ups

ALTER TABLE powerunits add COLUMN referenceid varchar(50) ;


# --- !Downs

ALTER TABLE powerunits  drop COLUMN referenceid ;
