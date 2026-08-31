package com.pizzapos.catalog.infrastructure.persistence.mapper;

import com.pizzapos.catalog.domain.model.PizzaIngredient;
import com.pizzapos.catalog.infrastructure.persistence.entity.PizzaIngredientEntity;
import com.pizzapos.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface PizzaIngredientPersistenceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pizza", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    PizzaIngredientEntity toEntity(PizzaIngredient pizzaIngredient);

    @Mapping(target = "pizzaId", source = "pizza.id")
    @Mapping(target = "ingredientId", source = "ingredient.id")
    @Mapping(target = "ingredientName", source = "ingredient.name")
    PizzaIngredient toDomain(PizzaIngredientEntity entity);

}
