-- Revert colored:table_image_colors from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS image_colors;

COMMIT;
