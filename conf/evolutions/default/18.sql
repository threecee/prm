# --- !Ups

ALTER TABLE powerstations add COLUMN referenceid int;
ALTER TABLE powerstations add COLUMN group_id int REFERENCES groups(id);
ALTER TABLE powerstations add COLUMN region int REFERENCES regions(id);


# --- !Downs

ALTER TABLE powerstations drop COLUMN referenceid ;
ALTER TABLE powerstations drop COLUMN group_id ;
ALTER TABLE powerstations drop COLUMN region ;
