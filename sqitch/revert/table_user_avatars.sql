-- Revert colored:table_user_avatars from mysql

BEGIN;

-- XXX Add DDLs here.

USE colored;

DROP TABLE IF EXISTS user_avatars;

COMMIT;
