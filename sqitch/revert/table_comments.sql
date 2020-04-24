-- Revert colored:table_comments from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS comments;

COMMIT;
