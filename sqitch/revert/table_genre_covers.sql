-- Revert colored:table_genre_covers from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS genre_covers;

COMMIT;
