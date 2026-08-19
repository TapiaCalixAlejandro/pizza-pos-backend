package com.pizzapos.catalog.infrastructure.persistence.mapper;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.infrastructure.persistence.entity.IngredientEntity;
import com.pizzapos.config.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface IngredientPersistenceMapper {

    IngredientEntity toEntity(Ingredient ingredient);

    Ingredient toDomain(IngredientEntity entity);

}
