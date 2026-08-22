package com.pizzapos.catalog.infrastructure.persistence.mapper;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.infrastructure.persistence.entity.IngredientEntity;
import com.pizzapos.config.CentralMapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface IngredientPersistenceMapper {

    IngredientEntity toEntity(Ingredient ingredient);

    Ingredient toDomain(IngredientEntity entity);

    List<IngredientEntity> toEntityList(List<Ingredient> ingredients);

    List<Ingredient> toDomainList(List<IngredientEntity> entities);

}
