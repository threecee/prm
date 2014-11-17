# --- !Ups

ALTER TABLE downtimecosts ALTER COLUMN powerunit DROP NOT NULL;
ALTER TABLE downtimecosts add COLUMN powerstation int references  powerstations(id);


# --- !Downs

ALTER TABLE downtimecosts  drop COLUMN powerstation ;
ALTER TABLE downtimecosts ALTER COLUMN powerunit SET NOT NULL;
