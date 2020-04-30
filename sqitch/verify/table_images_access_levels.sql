-- Verify colored:table_images_access_levels on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM images_access_levels limit 1;

ROLLBACK;
