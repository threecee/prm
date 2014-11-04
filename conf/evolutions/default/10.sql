# --- !Ups

ALTER TABLE repairs ALTER COLUMN probability type double precision ;


# --- !Downs

ALTER TABLE repairs ALTER COLUMN probability type int ;
