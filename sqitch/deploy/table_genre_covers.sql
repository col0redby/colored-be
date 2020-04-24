-- Deploy colored:table_genre_covers to mysql
-- requires: database_colored
-- requires: table_genres

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS genre_covers 
(
  genre_id   MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  xs         VARCHAR(255),
  sm         VARCHAR(255),
  md         VARCHAR(255),
  FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE
);

COMMIT;
