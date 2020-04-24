-- Revert colored:database_colored from mysql

BEGIN;

-- XXX Add DDLs here.

DROP DATABASE IF EXISTS colored;

COMMIT;
