-- Verify colored:table_likes on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM likes limit 1;

ROLLBACK;
