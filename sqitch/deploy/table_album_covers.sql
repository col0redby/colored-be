-- Deploy colored:table_album_covers to mysql
-- requires: database_colored
-- requires: table_albums

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS album_covers 
(
  album_id   MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  xs         VARCHAR(255),
  sm         VARCHAR(255),
  md         VARCHAR(255),
  FOREIGN KEY (album_id) REFERENCES albums (id) ON DELETE CASCADE
);

COMMIT;
