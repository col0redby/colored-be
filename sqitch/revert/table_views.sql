-- Revert colored:table_views from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS views;

COMMIT;
