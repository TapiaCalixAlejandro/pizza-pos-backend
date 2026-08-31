package com.pizzapos.catalog.domain.ports.in.pizza;

import com.pizzapos.catalog.domain.model.Pizza;

public interface CreatePizzaUseCase {

    Pizza createPizza(Pizza pizza);

}
