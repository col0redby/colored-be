-- Deploy colored:table_image_colors to mysql
-- requires: database_colored
-- requires: table_images

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS image_colors 
(
  id         MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  INDEX image_ind(image_id),
  image_id   MEDIUMINT,
  pct        FLOAT NOT NULL,
  color      CHAR(7),
  FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE cascade
);

COMMIT;
