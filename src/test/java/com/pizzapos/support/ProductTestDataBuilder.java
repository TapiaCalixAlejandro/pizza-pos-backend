package com.pizzapos.support;

import com.pizzapos.catalog.domain.enums.ProductStatus;
import com.pizzapos.catalog.domain.enums.ProductType;
import com.pizzapos.catalog.domain.model.Product;

import java.math.BigDecimal;

public class ProductTestDataBuilder {

    private final Product product;

    private ProductTestDataBuilder() {
        product = new Product();

        product.setName("Pepperoni");
        product.setDescription("Classic Pepperoni");
        product.setPrice(new BigDecimal("199.99"));
        product.setImage("pepperoni.png");
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setProductType(ProductType.PIZZA);
    }

    public static ProductTestDataBuilder aPizza() {
        return new ProductTestDataBuilder();
    }

    public ProductTestDataBuilder withName(String name) {
        product.setName(name);
        return this;
    }

    public ProductTestDataBuilder withPrice(BigDecimal price) {
        product.setPrice(price);
        return this;
    }

    public ProductTestDataBuilder withDescription(String description) {
        product.setDescription(description);
        return this;
    }

    public ProductTestDataBuilder withStatus(ProductStatus status) {
        product.setProductStatus(status);
        return this;
    }

    public Product build() {
        return product;
    }

}
