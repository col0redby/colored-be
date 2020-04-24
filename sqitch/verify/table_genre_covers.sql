-- Verify colored:table_genre_covers on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM genre_covers limit 1;

ROLLBACK;
