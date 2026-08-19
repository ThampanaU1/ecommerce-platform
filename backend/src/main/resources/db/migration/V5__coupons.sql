CREATE TABLE coupons (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         code VARCHAR(50) NOT NULL,
                         type VARCHAR(20) NOT NULL,
                         value DECIMAL(12,2) NOT NULL,
                         min_order_value DECIMAL(12,2) NOT NULL DEFAULT 0,
                         max_discount DECIMAL(12,2) NULL,
                         usage_limit_total INT NULL,
                         usage_limit_per_user INT NULL,
                         start_date TIMESTAMP NOT NULL,
                         end_date TIMESTAMP NOT NULL,
                         is_active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (id),
                         UNIQUE KEY uk_coupons_code (code)
);

CREATE TABLE coupon_redemptions (
                                    id BIGINT NOT NULL AUTO_INCREMENT,
                                    coupon_id BIGINT NOT NULL,
                                    user_id BIGINT NOT NULL,
                                    order_id BIGINT NOT NULL,
                                    redeemed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (id),
                                    CONSTRAINT fk_coupon_redemptions_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id),
                                    CONSTRAINT fk_coupon_redemptions_user FOREIGN KEY (user_id) REFERENCES users(id),
                                    CONSTRAINT fk_coupon_redemptions_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

ALTER TABLE orders ADD COLUMN coupon_id BIGINT NULL AFTER shipping_address_id;
ALTER TABLE orders ADD COLUMN discount_total DECIMAL(12,2) NOT NULL DEFAULT 0 AFTER subtotal;
ALTER TABLE orders ADD CONSTRAINT fk_orders_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id);