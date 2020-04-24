-- Deploy colored:table_genres to mysql
-- requires: database_colored

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS genres 
(
  id          MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(50),
  image_url   VARCHAR(255)
);

COMMIT;
