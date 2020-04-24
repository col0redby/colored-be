-- Revert colored:table_image_tags from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS image_tags;

COMMIT;
