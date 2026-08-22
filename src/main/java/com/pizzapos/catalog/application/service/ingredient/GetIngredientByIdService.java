package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.in.ingredient.GetIngredientByIdUseCase;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.ResourceNotFoundException;

public class GetIngredientByIdService implements GetIngredientByIdUseCase {

    private final IngredientRepositoryPort ingredientRepositoryPort;

    public GetIngredientByIdService(IngredientRepositoryPort ingredientRepositoryPort) {
        this.ingredientRepositoryPort = ingredientRepositoryPort;
    }

    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.INGREDIENT_NOT_FOUND));
    }
}
