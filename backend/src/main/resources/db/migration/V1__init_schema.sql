CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(50) NOT NULL,
                       PRIMARY KEY (id),
                       UNIQUE KEY uk_roles_name (name)
);

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(150) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (id),
                       UNIQUE KEY uk_users_email (email)
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE categories (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            name VARCHAR(150) NOT NULL,
                            description TEXT,
                            image_url VARCHAR(500),
                            display_order INT NOT NULL DEFAULT 0,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (id),
                            UNIQUE KEY uk_categories_name (name)
);

CREATE TABLE products (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          sku VARCHAR(100) NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          category_id BIGINT NOT NULL,
                          status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
                          featured BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_products_sku (sku),
                          CONSTRAINT fk_products_category
                              FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE product_images (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                product_id BIGINT NOT NULL,
                                image_url VARCHAR(500) NOT NULL,
                                is_primary BOOLEAN NOT NULL DEFAULT FALSE,
                                display_order INT NOT NULL DEFAULT 0,
                                PRIMARY KEY (id),
                                CONSTRAINT fk_product_images_product
                                    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE product_prices (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                product_id BIGINT NOT NULL,
                                mrp DECIMAL(12,2) NOT NULL,
                                selling_price DECIMAL(12,2) NOT NULL,
                                active BOOLEAN NOT NULL DEFAULT TRUE,
                                valid_from DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                valid_to DATETIME NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (id),
                                CONSTRAINT fk_product_prices_product
                                    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE inventory (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           product_id BIGINT NOT NULL,
                           available_quantity INT NOT NULL DEFAULT 0,
                           reserved_quantity INT NOT NULL DEFAULT 0,
                           reorder_level INT NOT NULL DEFAULT 0,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (id),
                           UNIQUE KEY uk_inventory_product (product_id),
                           CONSTRAINT fk_inventory_product
                               FOREIGN KEY (product_id) REFERENCES products(id)
);