package com.pizzapos.catalog.presentation.mapper;

import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.model.PizzaIngredient;
import com.pizzapos.catalog.presentation.dto.pizza.request.CreatePizzaIngredientRequest;
import com.pizzapos.catalog.presentation.dto.pizza.request.CreatePizzaRequest;
import com.pizzapos.catalog.presentation.dto.pizza.response.PizzaIngredientResponse;
import com.pizzapos.catalog.presentation.dto.pizza.response.PizzaResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PizzaPresentationMapperTest {

    private final PizzaPresentationMapper mapper =
            Mappers.getMapper(PizzaPresentationMapper.class);

    @Test
    void shouldMapCreatePizzaRequestToDomain() {
        CreatePizzaIngredientRequest ingredientRequest =
                new CreatePizzaIngredientRequest();

        ingredientRequest.setIngredientId(4L);
        ingredientRequest.setQuantity(new BigDecimal("0.250"));

        CreatePizzaRequest request = new CreatePizzaRequest();

        request.setProductId(1L);
        request.setPreparationTime(20);
        request.setIngredients(List.of(ingredientRequest));

        Pizza result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals(20, result.getPreparationTime());

        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());

        PizzaIngredient ingredient = result.getIngredients().get(0);

        assertEquals(4L, ingredient.getIngredientId());
        assertEquals(
                new BigDecimal("0.250"),
                ingredient.getQuantity()
        );
    }

    @Test
    void shouldMapCreatePizzaIngredientRequestToDomain() {

        CreatePizzaIngredientRequest request =
                new CreatePizzaIngredientRequest();

        request.setIngredientId(4L);
        request.setQuantity(new BigDecimal("0.250"));

        PizzaIngredient result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals(4L, result.getIngredientId());
        assertEquals(
                new BigDecimal("0.250"),
                result.getQuantity()
        );

        assertNull(result.getId());
        assertNull(result.getPizzaId());
        assertNull(result.getIngredientName());
    }

    @Test
    void shouldMapPizzaToResponse() {

        PizzaIngredient ingredient = new PizzaIngredient();

        ingredient.setId(10L);
        ingredient.setPizzaId(5L);
        ingredient.setIngredientId(4L);
        ingredient.setIngredientName("Mozzarella");
        ingredient.setQuantity(new BigDecimal("0.250"));

        Pizza pizza = new Pizza();

        pizza.setId(5L);
        pizza.setProductId(1L);
        pizza.setPreparationTime(20);
        pizza.setIngredients(List.of(ingredient));

        PizzaResponse result = mapper.toResponse(pizza);

        assertNotNull(result);

        assertEquals(5L, result.getId());
        assertEquals(1L, result.getProductId());
        assertEquals(20, result.getPreparationTime());

        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());

        PizzaIngredientResponse ingredientResponse =
                result.getIngredients().get(0);

        assertEquals(4L, ingredientResponse.getIngredientId());
        assertEquals("Mozzarella", ingredientResponse.getIngredientName());
        assertEquals(
                new BigDecimal("0.250"),
                ingredientResponse.getQuantity()
        );
    }

    @Test
    void shouldMapPizzaIngredientToResponse() {

        PizzaIngredient ingredient = new PizzaIngredient();

        ingredient.setId(10L);
        ingredient.setPizzaId(5L);
        ingredient.setIngredientId(4L);
        ingredient.setIngredientName("Mozzarella");
        ingredient.setQuantity(new BigDecimal("0.250"));

        PizzaIngredientResponse result =
                mapper.toResponse(ingredient);

        assertNotNull(result);

        assertEquals(4L, result.getIngredientId());
        assertEquals("Mozzarella", result.getIngredientName());
        assertEquals(
                new BigDecimal("0.250"),
                result.getQuantity()
        );
    }

    @Test
    void shouldMapPizzaWithMultipleIngredientsToResponse() {

        PizzaIngredient cheese = new PizzaIngredient();
        cheese.setIngredientId(4L);
        cheese.setIngredientName("Mozzarella");
        cheese.setQuantity(new BigDecimal("0.250"));

        PizzaIngredient tomato = new PizzaIngredient();
        tomato.setIngredientId(5L);
        tomato.setIngredientName("Tomato");
        tomato.setQuantity(new BigDecimal("0.150"));

        Pizza pizza = new Pizza();

        pizza.setId(5L);
        pizza.setProductId(1L);
        pizza.setPreparationTime(20);
        pizza.setIngredients(List.of(cheese, tomato));

        PizzaResponse result = mapper.toResponse(pizza);

        assertNotNull(result);
        assertNotNull(result.getIngredients());

        assertEquals(2, result.getIngredients().size());

        assertEquals(
                4L,
                result.getIngredients().get(0).getIngredientId()
        );
        assertEquals(
                "Mozzarella",
                result.getIngredients().get(0).getIngredientName()
        );
        assertEquals(
                new BigDecimal("0.250"),
                result.getIngredients().get(0).getQuantity()
        );

        assertEquals(
                5L,
                result.getIngredients().get(1).getIngredientId()
        );
        assertEquals(
                "Tomato",
                result.getIngredients().get(1).getIngredientName()
        );
        assertEquals(
                new BigDecimal("0.150"),
                result.getIngredients().get(1).getQuantity()
        );
    }
}