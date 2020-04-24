-- Verify colored:table_image_colors on mysql

BEGIN;

-- XXX Add verifications here.

USE colored;

SELECT * FROM image_colors limit 1;

ROLLBACK;
