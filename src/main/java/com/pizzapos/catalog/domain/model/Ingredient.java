package com.pizzapos.catalog.domain.model;

import com.pizzapos.catalog.domain.enums.IngredientStatus;
import com.pizzapos.catalog.domain.enums.IngredientUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ingredient {

    private Long id;

    private String name;

    private IngredientUnit unit;

    private BigDecimal stock;

    private BigDecimal minimumStock;

    private IngredientStatus status;

    private BigDecimal cost;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public Ingredient() {
    }

    public Ingredient(
            Long id,
            String name,
            IngredientUnit unit,
            BigDecimal stock,
            BigDecimal minimumStock,
            IngredientStatus status,
            BigDecimal cost,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.stock = stock;
        this.minimumStock = minimumStock;
        this.status = status;
        this.cost = cost;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
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

    public IngredientUnit getUnit() {
        return unit;
    }

    public void setUnit(IngredientUnit unit) {
        this.unit = unit;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public IngredientStatus getStatus() {
        return status;
    }

    public void setStatus(IngredientStatus status) {
        this.status = status;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
