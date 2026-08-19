CREATE TABLE banners (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         title VARCHAR(200),
                         image_url VARCHAR(500) NOT NULL,
                         link_url VARCHAR(500),
                         display_order INT NOT NULL DEFAULT 0,
                         is_active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (id)
);