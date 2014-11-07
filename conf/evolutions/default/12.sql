# --- !Ups

ALTER TABLE downtimecosts add COLUMN planned boolean not null;


# --- !Downs

ALTER TABLE downtimecosts  drop COLUMN planned ;
