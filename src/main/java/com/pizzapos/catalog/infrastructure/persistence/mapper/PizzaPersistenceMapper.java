package com.pizzapos.catalog.infrastructure.persistence.mapper;

import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.infrastructure.persistence.entity.PizzaEntity;
import com.pizzapos.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CentralMapperConfig.class,
        uses = PizzaIngredientPersistenceMapper.class
)
public interface PizzaPersistenceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    PizzaEntity toEntity(Pizza pizza);

    @Mapping(target = "ingredients", ignore = true)
    Pizza toDomain(PizzaEntity entity);

}
