-- Revert colored:table_tags from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS tags;

COMMIT;
