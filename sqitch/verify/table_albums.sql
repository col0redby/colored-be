-- Verify colored:table_albums on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM albums limit 1;

ROLLBACK;
