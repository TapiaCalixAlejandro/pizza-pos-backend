package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import com.pizzapos.support.IngredientTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UpdateIngredientServiceTest {

    @Mock
    private IngredientRepositoryPort repositoryPort;

    private UpdateIngredientService service;

    @BeforeEach
    void setUp() {
        service = new UpdateIngredientService(repositoryPort);
    }

    @Test
    @DisplayName("Should update ingredient successfully")
    void shouldUpdateIngredientSuccessfully() {
        // Given
        Ingredient exists = IngredientTestDataBuilder
                .anIngredient()
                .build();

        exists.setId(1L);

        Ingredient request = IngredientTestDataBuilder
                .anIngredient()
                .withName("Mass")
                .build();

        request.setCost(new BigDecimal("190.00"));

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(exists));
        when(repositoryPort.findByName("Mass")).thenReturn(Optional.empty());
        when(repositoryPort.save(exists)).thenReturn(exists);

        // When
        Ingredient result = service.updateIngredient(1L, request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mass", result.getName());
        assertEquals(new BigDecimal("190.00"), result.getCost());

        verify(repositoryPort).findById(1L);
        verify(repositoryPort).findByName("Mass");
        verify(repositoryPort).save(exists);
    }

    @Test
    @DisplayName("Should throw exception when ingredient does not exist")
    void shouldThrowExceptionWhenIngredientDoesNotExist() {
        // Given
        Ingredient request = IngredientTestDataBuilder
                .anIngredient()
                .build();

        when(repositoryPort.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateIngredient(999L, request)
        );

        assertEquals(Messages.INGREDIENT_NOT_FOUND, exception.getMessage());

        verify(repositoryPort).findById(999L);
        verify(repositoryPort, never()).findByName(anyString());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when ingredient name already exist")
    void shouldThrowExceptionWhenIngredientNameAlreadyExist() {
        // Given
        Ingredient exists = IngredientTestDataBuilder
                .anIngredient()
                .build();

        exists.setId(1L);

        Ingredient another = IngredientTestDataBuilder
                .anIngredient()
                .withName("Mass")
                .build();

        another.setId(2L);

        Ingredient request = IngredientTestDataBuilder
                .anIngredient()
                .withName("Mass")
                .build();

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(exists));
        when(repositoryPort.findByName("Mass")).thenReturn(Optional.of(another));

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.updateIngredient(1L, request)
        );

        assertEquals(Messages.INGREDIENT_ALREADY_EXISTS, exception.getMessage());

        verify(repositoryPort).findById(1L);
        verify(repositoryPort).findByName("Mass");
        verify(repositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should allow update when ingredient keeps its current name")
    void shouldAllowUpdateWhenIngredientKeepsItsCurrentName() {
        // Given
        Ingredient exists = IngredientTestDataBuilder
                .anIngredient()
                .build();

        exists.setId(1L);
        exists.setCost(new BigDecimal("190.00"));

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(exists));
        when(repositoryPort.findByName("Mozzarella")).thenReturn(Optional.of(exists));
        when(repositoryPort.save(exists)).thenReturn(exists);

        // When
        Ingredient result = service.updateIngredient(1L, exists);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mozzarella", result.getName());
        assertEquals(new BigDecimal("190.00"), result.getCost());

        verify(repositoryPort).findById(1L);
        verify(repositoryPort).findByName("Mozzarella");
        verify(repositoryPort).save(exists);
    }

}
