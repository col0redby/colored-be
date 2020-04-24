-- Verify colored:table_album_covers on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM album_covers limit 1;

ROLLBACK;
