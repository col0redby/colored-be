-- Verify colored:table_image_metadata on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM image_metadata limit 1;

ROLLBACK;
