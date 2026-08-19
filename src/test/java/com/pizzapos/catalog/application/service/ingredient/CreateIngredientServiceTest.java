package com.pizzapos.catalog.application.service.ingredient;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.shared.exception.BusinessException;
import com.pizzapos.support.IngredientTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateIngredientServiceTest {

    @Mock
    private IngredientRepositoryPort repositoryPort;

    @InjectMocks
    private CreateIngredientService ingredientService;

    @Test
    @DisplayName("Should create ingredient successfully")
    void shouldCreateIngredientSuccessfully() {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        when(repositoryPort.findByName(ingredient.getName()))
                .thenReturn(Optional.empty());
        when(repositoryPort.save(ingredient))
                .thenReturn(ingredient);

        // When
        Ingredient saved = ingredientService.createIngredient(ingredient);
        saved.setId(1L);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo("Mozzarella");
        assertThat(saved.getCost()).isEqualByComparingTo("180.00");

        verify(repositoryPort).findByName("Mozzarella");
        verify(repositoryPort).save(ingredient);
    }

    @Test
    @DisplayName("Should throw exception when ingredient already exists")
    void shouldThrowExceptionWhenIngredientAlreadyExists() {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        when(repositoryPort.findByName(ingredient.getName()))
                .thenReturn(Optional.of(ingredient));

        // When & Then
        assertThrows(
                BusinessException.class,
                () -> ingredientService.createIngredient(ingredient)
        );

        verify(repositoryPort).findByName("Mozzarella");
        verify(repositoryPort, never()).save(any(Ingredient.class));
    }

}
