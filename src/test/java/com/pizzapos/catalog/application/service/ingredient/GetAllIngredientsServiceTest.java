package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.support.IngredientTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GetAllIngredientsServiceTest {

    @Mock
    private IngredientRepositoryPort repositoryPort;

    private GetAllIngredientsService service;

    @BeforeEach
    void setUp() {
        service = new GetAllIngredientsService(repositoryPort);
    }

    @Test
    @DisplayName("Should return all ingredients")
    void shouldReturnAllIngredients() {
        // Given
        Ingredient mozzarella = IngredientTestDataBuilder
                .anIngredient()
                .build();

        Ingredient mass = IngredientTestDataBuilder
                .anIngredient()
                .withName("Mass")
                .build();

        when(repositoryPort.findAll()).thenReturn(List.of(mozzarella, mass));

        // When
        List<Ingredient> result = service.getAllIngredients();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mozzarella", result.get(0).getName());
        assertEquals("Mass", result.get(1).getName());

        verify(repositoryPort, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when there are no ingredients")
    void shouldReturnEmptyListWhenThereAreNoIngredients() {
        // Given
        when(repositoryPort.findAll()).thenReturn(List.of());

        // When
        List<Ingredient> result = service.getAllIngredients();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(repositoryPort, times(1)).findAll();
    }

}
