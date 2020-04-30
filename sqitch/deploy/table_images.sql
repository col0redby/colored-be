-- Deploy colored:table_images to mysql
-- requires: database_colored
-- requires: table_users
-- requires: table_albums
-- requires: table_genres

BEGIN;

-- XXX Add DDLs here.

USE colored;

CREATE TABLE IF NOT EXISTS images
(
    id              MEDIUMINT AUTO_INCREMENT PRIMARY KEY,
    FULLTEXT INDEX title_ind (title),
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    width           INT,
    height          INT,
    xs              VARCHAR(255),
    sm              VARCHAR(255),
    md              VARCHAR(255),
    lg              VARCHAR(255),
    original        VARCHAR(255),
    INDEX user_ind (user_id),
    user_id         MEDIUMINT,
    album_id        MEDIUMINT,
    genre_id        MEDIUMINT,
    access_level_id TINYINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (album_id) REFERENCES albums (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE,
    FOREIGN KEY (access_level_id) REFERENCES images_access_levels (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

COMMIT;
