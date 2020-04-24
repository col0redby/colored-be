-- Verify colored:table_images on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM images limit 1;

ROLLBACK;
