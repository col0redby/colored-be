-- Deploy colored:table_users to mysql
-- requires: database_colored

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS users 
(
  id         MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  FULLTEXT INDEX username_ind (username),
  username   VARCHAR(50),
  email      VARCHAR(320),
  country    VARCHAR(320),
  birthday   DATE
);

COMMIT;
