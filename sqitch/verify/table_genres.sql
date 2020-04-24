-- Verify colored:table_genres on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM genres limit 1;

ROLLBACK;
