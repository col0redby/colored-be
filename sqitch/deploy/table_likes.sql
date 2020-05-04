-- Deploy colored:table_likes to mysql
-- requires: database_colored
-- requires: table_images
-- requires: table_users

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS likes 
(
  image_id   MEDIUMINT,
  user_id    MEDIUMINT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (image_id, user_id),
  FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

COMMIT;
