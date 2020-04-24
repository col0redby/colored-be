-- Deploy colored:table_image_tags to mysql
-- requires: database_colored
-- requires: table_images
-- requires: table_tags

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS image_tags 
(
  id         MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  image_id   MEDIUMINT,
  INDEX tag_ind(tag_id),
  tag_id     MEDIUMINT,
  FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

COMMIT;
