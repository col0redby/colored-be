-- Deploy colored:table_likes to mysql
-- requires: database_colored
-- requires: table_images
-- requires: table_users

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS likes 
(
  id         MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  INDEX image_ind(image_id),
  image_id   MEDIUMINT,
  user_id    MEDIUMINT,
  ts         TIMESTAMP,
  FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE no action ON UPDATE no action,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE no action ON UPDATE no action
);

COMMIT;
