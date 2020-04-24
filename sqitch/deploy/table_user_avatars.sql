-- Deploy colored:table_user_avatars to mysql
-- requires: database_colored
-- requires: table_users

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS user_avatars 
(
  user_id   MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  xs        VARCHAR(255),
  sm        VARCHAR(255),
  md        VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

COMMIT;
