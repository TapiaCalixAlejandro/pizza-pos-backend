CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(150) NOT NULL,

    unit VARCHAR(30) NOT NULL,

    stock NUMERIC(12, 3) NOT NULL,

    minimum_stock NUMERIC(12, 3) NOT NULL,

    status VARCHAR(30) NOT NULL,

    cost NUMERIC(12, 2) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    deleted_at TIMESTAMP NULL
);

CREATE UNIQUE INDEX uk_ingredients_name_active
ON ingredients (name)
WHERE deleted_at IS NULL;