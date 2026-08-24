package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.ports.in.ingredient.DeleteIngredientUseCase;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.ResourceNotFoundException;

public class DeleteIngredientService implements DeleteIngredientUseCase {

    private IngredientRepositoryPort repositoryPort;

    public DeleteIngredientService(IngredientRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public void deletedIngredientById(Long id) {
        repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.INGREDIENT_NOT_FOUND));

        repositoryPort.deleteById(id);
    }

}
