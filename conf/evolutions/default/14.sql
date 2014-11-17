# --- !Ups

ALTER TABLE components ALTER COLUMN powerunit DROP NOT NULL;
ALTER TABLE components add COLUMN powerstation int references  powerstations(id);


# --- !Downs

ALTER TABLE components  drop COLUMN powerstation ;
ALTER TABLE components ALTER COLUMN powerunit SET NOT NULL;
