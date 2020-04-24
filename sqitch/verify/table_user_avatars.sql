-- Verify colored:table_user_avatars on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM user_avatars limit 1;

ROLLBACK;
