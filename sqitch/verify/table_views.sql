-- Verify colored:table_views on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM views limit 1;

ROLLBACK;
