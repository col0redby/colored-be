-- Verify colored:table_tags on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM tags limit 1;

ROLLBACK;
