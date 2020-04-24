-- Revert colored:table_images from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS images;

COMMIT;
