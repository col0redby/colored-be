-- Verify colored:table_users on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM users limit 1;

ROLLBACK;
