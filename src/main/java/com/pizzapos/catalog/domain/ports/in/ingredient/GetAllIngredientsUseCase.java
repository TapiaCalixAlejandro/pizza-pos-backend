package com.pizzapos.catalog.domain.ports.in.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;

import java.util.List;

public interface GetAllIngredientsUseCase {

    List<Ingredient> getAllIngredients();

}
