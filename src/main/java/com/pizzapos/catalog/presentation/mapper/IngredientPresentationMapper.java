package com.pizzapos.catalog.presentation.mapper;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.presentation.dto.ingredient.request.CreateIngredientRequest;
import com.pizzapos.catalog.presentation.dto.ingredient.response.IngredientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientPresentationMapper {
    
    @Mapping(target = "status", constant = "ACTIVE")
    Ingredient toDomain(CreateIngredientRequest request);

    IngredientResponse toResponse(Ingredient ingredient);

    List<IngredientResponse> toResponseList(List<Ingredient> ingredients);

}
