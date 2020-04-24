-- Revert colored:table_genres from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS genres;

COMMIT;
