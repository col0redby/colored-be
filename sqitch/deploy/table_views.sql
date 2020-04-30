-- Deploy colored:table_views to mysql
-- requires: database_colored
-- requires: table_images
-- requires: table_users

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS views 
(
  id         MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  INDEX image_ind(image_id),
  image_id   MEDIUMINT,
  user_id    MEDIUMINT,
  dt         DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

COMMIT;
