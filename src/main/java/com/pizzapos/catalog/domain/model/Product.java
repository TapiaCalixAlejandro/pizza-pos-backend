package com.pizzapos.catalog.domain.model;

import com.pizzapos.catalog.domain.enums.ProductStatus;
import com.pizzapos.catalog.domain.enums.ProductType;

import java.math.BigDecimal;

public class Product {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String image;

    private ProductType productType;

    private ProductStatus productStatus;

    public Product() {
    }

    public Product(
            Long id,
            String name,
            String description,
            BigDecimal price,
            String image,
            ProductType productType,
            ProductStatus productStatus
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.productType = productType;
        this.productStatus = productStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

}
