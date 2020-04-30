BEGIN;

USE colored;

INSERT INTO users (username, email, country, birthday)
VALUES ('nkharitonov', 'a@a.a', 'Belarus', '1999-01-16');

INSERT INTO genres (title)
VALUES ('Animals');

INSERT INTO images_access_levels(id, level, description)
VALUES (1, 'public', 'Public access level, images available for all users.');
INSERT INTO images_access_levels(id, level, description)
VALUES (2, 'private', 'Private access level, images available just for owner.');

INSERT INTO images(title, description, original, user_id, genre_id, access_level_id)
VALUES (
           'Seed image',
           'Default image, was taken in Idea on Lenovo computer in 10:57:48 UTC, description...',
           'https://colored-backend-eu-north-1.s3.eu-north-1.amazonaws.com/nkharitonov/original/DSC_0835-small.jpg',
           1, 1, 1
       );

INSERT INTO likes(image_id, user_id)
VALUES (1, 1);

INSERT INTO comments(image_id, user_id, text)
VALUES (1, 1, 'This is a test comment');

INSERT INTO views(image_id, user_id, dt)
VALUES (1, 1, current_timestamp);
INSERT INTO views(image_id, user_id, dt)
VALUES (1, 1, current_timestamp);
INSERT INTO views(image_id, user_id, dt)
VALUES (1, 1, current_timestamp);

INSERT INTO image_colors(image_id, pct, color)
VALUES (1, 50, '#123456');
INSERT INTO image_colors(image_id, pct, color)
VALUES (1, 30, '#654321');
INSERT INTO image_colors(image_id, pct, color)
VALUES (1, 20, '#789012');

INSERT INTO image_metadata(image_id, exposure_time_description, exposure_time_inverse, iso_description, iso, aperture_desctiprion, aperture, gps_latitude_description, gps_latitude, gps_longitude_description, gps_longitude, gps_altitude_description, gps_altitude_meters)
VALUES (1, '1/200 sec', 200, '100', 100, 'f/4.5', 4.5, '45 45 45', 0.454545, '54 54 54', 0.545454, '260.3 metres', 260.3);

INSERT INTO tags(title) VALUES ('Portrait');
INSERT INTO image_tags(image_id, tag_id) VALUES (1, 1);

COMMIT;
