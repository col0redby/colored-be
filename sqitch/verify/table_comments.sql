-- Verify colored:table_comments on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM comments limit 1;

ROLLBACK;
