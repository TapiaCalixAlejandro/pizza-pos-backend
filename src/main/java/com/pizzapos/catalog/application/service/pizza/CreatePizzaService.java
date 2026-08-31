package com.pizzapos.catalog.application.service.pizza;

import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.model.PizzaIngredient;
import com.pizzapos.catalog.domain.ports.in.pizza.CreatePizzaUseCase;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.catalog.domain.ports.out.PizzaRepositoryPort;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class CreatePizzaService implements CreatePizzaUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePizzaService.class);

    private final PizzaRepositoryPort pizzaRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final IngredientRepositoryPort ingredientRepositoryPort;

    public CreatePizzaService(
            PizzaRepositoryPort pizzaRepositoryPort,
            ProductRepositoryPort productRepositoryPort,
            IngredientRepositoryPort ingredientRepositoryPort
    ) {
        this.pizzaRepositoryPort = pizzaRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
        this.ingredientRepositoryPort = ingredientRepositoryPort;
    }

    @Override
    @Transactional
    public Pizza createPizza(Pizza pizza) {
        LOGGER.info(
                "Creating pizza [productId={}, preparationTime={}]",
                pizza.getProductId(),
                pizza.getPreparationTime()
        );

        productRepositoryPort.
                findById(pizza.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(Messages.PRODUCT_NOT_FOUND)
                );

        if (pizzaRepositoryPort.findByProductId(pizza.getProductId()).isPresent()) {

            throw new BusinessException(Messages.PIZZA_ALREADY_EXISTS);
        }

        if (pizza.getIngredients() == null || pizza.getIngredients().isEmpty()) {

            throw new BusinessException(Messages.PIZZA_MUST_HAVE_INGREDIENTS);
        }

        for (PizzaIngredient pizzaIngredient : pizza.getIngredients()) {
            ingredientRepositoryPort
                    .findById(pizzaIngredient.getIngredientId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(Messages.INGREDIENT_NOT_FOUND)
                    );
        }

        Pizza saved = pizzaRepositoryPort.createPizza(pizza);

        LOGGER.info(
                "Pizza created successfully [id={}, productId={}]",
                saved.getId(),
                saved.getProductId()
        );

        return saved;
    }

}
