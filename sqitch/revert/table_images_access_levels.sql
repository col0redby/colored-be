-- Revert colored:table_images_access_levels from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS images_access_levels;

COMMIT;
