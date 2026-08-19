CREATE TABLE shipping_rules (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                name VARCHAR(150) NOT NULL,
                                region_pattern VARCHAR(100) NOT NULL DEFAULT '*',
                                min_order_free_shipping DECIMAL(12,2) NULL,
                                base_charge DECIMAL(12,2) NOT NULL DEFAULT 0,
                                is_active BOOLEAN NOT NULL DEFAULT TRUE,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (id)
);

CREATE TABLE tax_configs (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             name VARCHAR(150) NOT NULL,
                             tax_percent DECIMAL(5,2) NOT NULL,
                             is_active BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (id)
);

INSERT INTO shipping_rules (name, region_pattern, min_order_free_shipping, base_charge, is_active)
VALUES ('Standard Nationwide', '*', 999.00, 49.00, TRUE);

INSERT INTO tax_configs (name, tax_percent, is_active)
VALUES ('Default GST', 18.00, TRUE);