package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.in.ingredient.CreateIngredientUseCase;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class CreateIngredientService implements CreateIngredientUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateIngredientService.class);
    private final IngredientRepositoryPort ingredientRepositoryPort;

    public CreateIngredientService(IngredientRepositoryPort ingredientRepositoryPort) {
        this.ingredientRepositoryPort = ingredientRepositoryPort;
    }

    @Override
    @Transactional
    public Ingredient createIngredient(Ingredient ingredient) {
        LOGGER.info(
                "Creating ingredient [name={}, unit={}, cost={}]",
                ingredient.getName(),
                ingredient.getUnit(),
                ingredient.getCost()
        );

        if (ingredientRepositoryPort.findByName(ingredient.getName()).isPresent()) {

            throw new BusinessException(Messages.INGREDIENT_ALREADY_EXISTS);
        }

        Ingredient savedIngredient = ingredientRepositoryPort.save(ingredient);

        LOGGER.info(
                "Ingredient created successfully [id={}, name={}]",
                savedIngredient.getId(),
                savedIngredient.getName()
        );

        return savedIngredient;
    }
}
