package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.in.ingredient.UpdateIngredientUseCase;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UpdateIngredientService implements UpdateIngredientUseCase {

    private final IngredientRepositoryPort repositoryPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateIngredientService.class);

    public UpdateIngredientService(IngredientRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Ingredient updateIngredient(Long id, Ingredient request) {
        LOGGER.info(
                "Update ingredient [name={}, unit={}, cost={}]",
                request.getName(),
                request.getUnit(),
                request.getCost()
        );

        Ingredient existingIngredient = repositoryPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Messages.INGREDIENT_NOT_FOUND)
                );

        Optional<Ingredient> ingredientWithSameName =
                repositoryPort.findByName(request.getName());

        if (ingredientWithSameName.isPresent() && !ingredientWithSameName.get().getId().equals(id)) {
            throw new BusinessException(Messages.INGREDIENT_ALREADY_EXISTS);
        }

        existingIngredient.setName(request.getName());
        existingIngredient.setUnit(request.getUnit());
        existingIngredient.setStock(request.getStock());
        existingIngredient.setMinimumStock(request.getMinimumStock());
        existingIngredient.setCost(request.getCost());

        LOGGER.info(
                "Ingredient updated successfully [id={}, name={}]",
                existingIngredient.getId(),
                existingIngredient.getName()
        );

        return repositoryPort.save(existingIngredient);
    }
}
