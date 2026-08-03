CREATE TABLE products
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    price NUMERIC(10, 2) NOT NULL,
    image VARCHAR(255),
    product_type VARCHAR(30) NOT NULL,
    product_status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE products
ADD CONSTRAINT uk_products_name
UNIQUE(name);