CREATE TABLE addresses (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           user_id BIGINT NOT NULL,
                           label VARCHAR(50),
                           line1 VARCHAR(255) NOT NULL,
                           line2 VARCHAR(255),
                           city VARCHAR(100) NOT NULL,
                           state VARCHAR(100) NOT NULL,
                           pincode VARCHAR(20) NOT NULL,
                           country VARCHAR(100) NOT NULL DEFAULT 'India',
                           is_default BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE orders (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        order_number VARCHAR(50) NOT NULL,
                        user_id BIGINT NOT NULL,
                        status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
                        subtotal DECIMAL(12,2) NOT NULL,
                        tax_total DECIMAL(12,2) NOT NULL DEFAULT 0,
                        shipping_total DECIMAL(12,2) NOT NULL DEFAULT 0,
                        grand_total DECIMAL(12,2) NOT NULL,
                        shipping_address_id BIGINT NOT NULL,
                        placed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        UNIQUE KEY uk_orders_order_number (order_number),
                        CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id),
                        CONSTRAINT fk_orders_address FOREIGN KEY (shipping_address_id) REFERENCES addresses(id)
);

CREATE TABLE order_items (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             product_name_snapshot VARCHAR(255) NOT NULL,
                             sku_snapshot VARCHAR(100) NOT NULL,
                             unit_price_snapshot DECIMAL(12,2) NOT NULL,
                             quantity INT NOT NULL,
                             line_total DECIMAL(12,2) NOT NULL,
                             PRIMARY KEY (id),
                             CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id),
                             CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);