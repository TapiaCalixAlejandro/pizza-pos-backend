package com.pizzapos.catalog.domain.ports.in.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;

public interface UpdateIngredientUseCase {

    Ingredient updateIngredient(Long id, Ingredient ingredient);

}
