package com.pizzapos.catalog.presentation.mapper;

import com.pizzapos.catalog.domain.enums.IngredientStatus;
import com.pizzapos.catalog.domain.enums.IngredientUnit;
import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.presentation.dto.ingredient.request.CreateIngredientRequest;
import com.pizzapos.catalog.presentation.dto.ingredient.response.IngredientResponse;
import com.pizzapos.support.IngredientTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IngredientPresentationMapperTest {

    private final IngredientPresentationMapper mapper =
            Mappers.getMapper(IngredientPresentationMapper.class);

    @Test
    @DisplayName("Should Mapper Ingredient to Ingredient Response")
    void shouldMapperIngredientToIngredientResponse() {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        // When
        IngredientResponse response = mapper.toResponse(ingredient);

        // Then
        assertNotNull(response);

        assertEquals(ingredient.getId(), response.getId());
        assertEquals("Mozzarella", response.getName());
        assertEquals(IngredientUnit.KILOGRAM, response.getUnit());
        assertEquals(new BigDecimal("20"), response.getStock());
        assertEquals(new BigDecimal("5"), response.getMinimumStock());
        assertEquals(new BigDecimal("180.00"), response.getCost());
        assertEquals(IngredientStatus.ACTIVE, response.getStatus());
    }

    @Test
    @DisplayName("Should Mapper Create Ingredient Request to Ingredient")
    void shouldMapperCreateIngredientRequestToIngredient() {
        // Given
        CreateIngredientRequest request = IngredientTestDataBuilder
                .anIngredient()
                .buildRequest();

        // When
        Ingredient ingredient = mapper.toDomain(request);

        // Then
        assertNotNull(ingredient);

        assertEquals("Mozzarella", ingredient.getName());
        assertEquals(IngredientUnit.KILOGRAM, ingredient.getUnit());
        assertEquals(new BigDecimal("20"), ingredient.getStock());
        assertEquals(new BigDecimal("5"), ingredient.getMinimumStock());
        assertEquals(new BigDecimal("180.00"), ingredient.getCost());
        assertEquals(IngredientStatus.ACTIVE, ingredient.getStatus());
    }

}
