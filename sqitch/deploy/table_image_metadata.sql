-- Deploy colored:table_image_metadata to mysql
-- requires: database_colored
-- requires: table_images

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS image_metadata 
(
  image_id                    MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
  exposure_time_description   VARCHAR(50),
  exposure_time_inverse       INT,
  iso_description             VARCHAR(50),
  iso                         INT,
  aperture_desctiprion        VARCHAR(50),
  aperture                    FLOAT,
  gps_latitude_description    VARCHAR(50),
  gps_latitude                DOUBLE,
  gps_longitude_description   VARCHAR(50),
  gps_longitude               DOUBLE,
  gps_altitude_description    VARCHAR(50),
  gps_altitude_meters         FLOAT,
  FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE CASCADE
);

COMMIT;
