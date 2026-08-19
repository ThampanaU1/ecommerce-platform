CREATE TABLE product_reviews (
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 product_id BIGINT NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 rating TINYINT NOT NULL,
                                 comment TEXT,
                                 status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (id),
                                 CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products(id),
                                 CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id),
                                 CONSTRAINT chk_rating_range CHECK (rating BETWEEN 1 AND 5)
);