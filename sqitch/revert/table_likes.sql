-- Revert colored:table_likes from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS likes;

COMMIT;
