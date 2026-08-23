package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.in.ingredient.GetAllIngredientsUseCase;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;

import java.util.List;

public class GetAllIngredientsService implements GetAllIngredientsUseCase {

    private final IngredientRepositoryPort repositoryPort;

    public GetAllIngredientsService(IngredientRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return repositoryPort.findAll();
    }

}
