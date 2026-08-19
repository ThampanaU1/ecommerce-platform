CREATE TABLE wishlists (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           user_id BIGINT NOT NULL,
                           product_id BIGINT NOT NULL,
                           added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (id),
                           UNIQUE KEY uk_wishlist_user_product (user_id, product_id),
                           CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(id),
                           CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES products(id)
);