ALTER TABLE products
DROP CONSTRAINT IF EXISTS uk_products_name;

DROP INDEX IF EXISTS uk_products_name;

CREATE UNIQUE INDEX uk_products_name_active
ON products (name)
WHERE deleted_at IS NULL;
