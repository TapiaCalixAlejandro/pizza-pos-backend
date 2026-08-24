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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DeleteIngredientByIdServiceTest {

    @Mock
    private IngredientRepositoryPort repositoryPort;

    @InjectMocks
    private DeleteIngredientService service;

    @Test
    @DisplayName("Should delete ingredient successfully when ingredient exists")
    void shouldDeleteIngredientSuccessfullyWhenIngredientExists() {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(ingredient));

        // When
        service.deletedIngredientById(1L);

        // Then
        verify(repositoryPort).findById(1L);
        verify(repositoryPort).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ingredient does not exist")
    void shouldThrowExceptionWhenIngredientDoesNotExist() {
        // Given
        Long ingredientId = 1L;

        when(repositoryPort.findById(ingredientId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deletedIngredientById(ingredientId)
        );

        assertEquals(Messages.INGREDIENT_NOT_FOUND, exception.getMessage());

        verify(repositoryPort).findById(ingredientId);
        verify(repositoryPort, never()).deleteById(ingredientId);
    }

}
