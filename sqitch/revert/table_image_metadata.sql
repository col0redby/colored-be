-- Revert colored:table_image_metadata from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS image_metadata;

COMMIT;
