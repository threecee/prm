# --- !Ups

ALTER TABLE repairs add COLUMN component int REFERENCES components(id) ;
ALTER TABLE repairs ALTER COLUMN componenttype DROP NOT NULL;
--ALTER TABLE repairs drop COLUMN componentype ;


# --- !Downs

ALTER TABLE repairs drop COLUMN component ;
ALTER TABLE repairs ALTER COLUMN componenenttype SET NOT NULL;
