-- Revert colored:table_albums from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS albums;

COMMIT;
