-- Deploy colored:table_tags to mysql
-- requires: database_colored

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS tags 
(
  id         MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  FULLTEXT INDEX title_ind (title),
  title      VARCHAR(50) NOT NULL
);

COMMIT;
