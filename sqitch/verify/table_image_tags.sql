-- Verify colored:table_image_tags on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM image_tags limit 1;

ROLLBACK;
