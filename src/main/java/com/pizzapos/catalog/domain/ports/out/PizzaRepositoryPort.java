package com.pizzapos.catalog.domain.ports.out;

import com.pizzapos.catalog.domain.model.Pizza;

import java.util.Optional;

public interface PizzaRepositoryPort {

    Pizza createPizza(Pizza pizza);

    Optional<Pizza> findByProductId(Long productId);

}
