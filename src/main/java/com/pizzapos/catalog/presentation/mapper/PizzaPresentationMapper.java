package com.pizzapos.catalog.presentation.mapper;

import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.model.PizzaIngredient;
import com.pizzapos.catalog.presentation.dto.pizza.request.CreatePizzaIngredientRequest;
import com.pizzapos.catalog.presentation.dto.pizza.request.CreatePizzaRequest;
import com.pizzapos.catalog.presentation.dto.pizza.response.PizzaIngredientResponse;
import com.pizzapos.catalog.presentation.dto.pizza.response.PizzaResponse;
import com.pizzapos.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface PizzaPresentationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Pizza toDomain(CreatePizzaRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pizzaId", ignore = true)
    @Mapping(target = "ingredientName", ignore = true)
    PizzaIngredient toDomain(CreatePizzaIngredientRequest request);

    PizzaResponse toResponse(Pizza pizza);

    PizzaIngredientResponse toResponse(PizzaIngredient pizzaIngredient);

}
