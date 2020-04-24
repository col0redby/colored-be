-- Revert colored:table_album_covers from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS album_covers;

COMMIT;
