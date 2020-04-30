-- Deploy colored:table_images_access_levels to mysql
-- requires: database_colored

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS images_access_levels
(
  id            TINYINT PRIMARY KEY,
  level         VARCHAR(50),
  description   VARCHAR(200),
  constraint check_title CHECK ( level in ('public', 'private') )
);

COMMIT;
