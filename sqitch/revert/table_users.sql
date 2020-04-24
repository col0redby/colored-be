-- Revert colored:table_users from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS users;

COMMIT;
