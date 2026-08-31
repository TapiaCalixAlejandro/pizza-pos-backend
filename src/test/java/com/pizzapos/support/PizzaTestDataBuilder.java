package com.pizzapos.support;

import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.model.PizzaIngredient;
import com.pizzapos.catalog.presentation.dto.pizza.request.CreatePizzaIngredientRequest;
import com.pizzapos.catalog.presentation.dto.pizza.request.CreatePizzaRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PizzaTestDataBuilder {

    private Long id;
    private Long productId = 1L;
    private Integer preparationTime = 20;
    private List<PizzaIngredient> ingredients = new ArrayList<>();

    private PizzaTestDataBuilder() {
        // Ingrediente por defecto
        PizzaIngredient ingredient = new PizzaIngredient();
        ingredient.setIngredientId(4L);
        ingredient.setQuantity(new BigDecimal("0.250"));

        ingredients.add(ingredient);
    }

    public static PizzaTestDataBuilder aPizza () {
        return new PizzaTestDataBuilder();
    }

    public PizzaTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PizzaTestDataBuilder withProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public PizzaTestDataBuilder withPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
        return this;
    }

    public PizzaTestDataBuilder withIngredients(List<PizzaIngredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public PizzaTestDataBuilder withIngredient(Long ingredientId, BigDecimal quantity) {
        PizzaIngredient ingredient = new PizzaIngredient();
        ingredient.setIngredientId(ingredientId);
        ingredient.setQuantity(quantity);

        this.ingredients.add(ingredient);

        return this;
    }

    public PizzaTestDataBuilder withoutIngredients() {
        this.ingredients = new ArrayList<>();
        return this;
    }

    public Pizza build() {
        Pizza pizza = new Pizza();
        pizza.setId(id);
        pizza.setProductId(productId);
        pizza.setPreparationTime(preparationTime);
        pizza.setIngredients(ingredients);

        return pizza;
    }

    public CreatePizzaRequest buildRequest() {
        CreatePizzaRequest request = new CreatePizzaRequest();
        request.setProductId(productId);
        request.setPreparationTime(preparationTime);

        List<CreatePizzaIngredientRequest> ingredientRequests = new ArrayList<>();

        for (PizzaIngredient ingredient : ingredients) {
            CreatePizzaIngredientRequest ingredientRequest = new CreatePizzaIngredientRequest();

            ingredientRequest.setIngredientId(ingredient.getIngredientId());
            ingredientRequest.setQuantity(ingredient.getQuantity());

            ingredientRequests.add(ingredientRequest);
        }

        request.setIngredients(ingredientRequests);

        return request;
    }

}
