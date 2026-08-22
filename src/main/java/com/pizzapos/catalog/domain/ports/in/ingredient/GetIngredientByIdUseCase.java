package com.pizzapos.catalog.domain.ports.in.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;

public interface GetIngredientByIdUseCase {

    Ingredient getIngredientById(Long id);

}
