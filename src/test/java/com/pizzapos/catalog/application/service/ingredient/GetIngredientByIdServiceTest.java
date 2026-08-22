package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import com.pizzapos.support.IngredientTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GetIngredientByIdServiceTest {

    @Mock
    private IngredientRepositoryPort repositoryPort;

    @InjectMocks
    private  GetIngredientByIdService byIdService;

    @Test
    @DisplayName("Should return ingredient when ingredient exists")
    void shouldReturnIngredientWhenIngredientExists() {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder.anIngredient().build();

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(ingredient));

        // When
        Ingredient result = byIdService.getIngredientById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Mozzarella", result.getName());

        verify(repositoryPort).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when ingredient does not exists")
    void shouldThrowExceptionWhenIngredientDoesNotExists() {
        // Given
        when(repositoryPort.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> byIdService.getIngredientById(1L)
                );

        assertEquals(Messages.INGREDIENT_NOT_FOUND, exception.getMessage());

        verify(repositoryPort).findById(1L);
    }

}
