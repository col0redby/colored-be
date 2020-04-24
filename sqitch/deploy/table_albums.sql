-- Deploy colored:table_albums to mysql
-- requires: database_colored

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS albums 
(
  id            MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  title         VARCHAR(200),
  description   text
);

COMMIT;
