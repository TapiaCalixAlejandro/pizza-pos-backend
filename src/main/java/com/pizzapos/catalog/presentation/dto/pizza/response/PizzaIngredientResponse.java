package com.pizzapos.catalog.presentation.dto.pizza.response;

import java.math.BigDecimal;

public class PizzaIngredientResponse {

    private Long ingredientId;
    private String ingredientName;
    private BigDecimal quantity;

    public PizzaIngredientResponse() {
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

}
