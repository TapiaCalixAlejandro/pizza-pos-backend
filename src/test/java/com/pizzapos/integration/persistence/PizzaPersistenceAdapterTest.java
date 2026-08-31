package com.pizzapos.integration.persistence;

import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.model.PizzaIngredient;
import com.pizzapos.catalog.infrastructure.persistence.adapter.PizzaPersistenceAdapter;
import com.pizzapos.catalog.infrastructure.persistence.entity.PizzaEntity;
import com.pizzapos.catalog.infrastructure.persistence.entity.PizzaIngredientEntity;
import com.pizzapos.catalog.infrastructure.persistence.mapper.PizzaIngredientPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.mapper.PizzaPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.repository.PizzaIngredientJpaRepository;
import com.pizzapos.catalog.infrastructure.persistence.repository.PizzaJpaRepository;
import com.pizzapos.support.PizzaTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PizzaPersistenceAdapterTest {

    @Mock
    private PizzaJpaRepository pizzaJpaRepository;

    @Mock
    private PizzaPersistenceMapper pizzaPersistenceMapper;

    @Mock
    private PizzaIngredientJpaRepository pizzaIngredientJpaRepository;

    @Mock
    private PizzaIngredientPersistenceMapper pizzaIngredientPersistenceMapper;

    @InjectMocks
    private PizzaPersistenceAdapter adapter;

    private Pizza pizza;
    private PizzaEntity pizzaEntity;

    @BeforeEach
    void setUp() {
        pizza = PizzaTestDataBuilder
                .aPizza()
                .withId(null)
                .build();

        pizzaEntity = new PizzaEntity();
        pizzaEntity.setId(1L);
        pizzaEntity.setProductId(1L);
        pizzaEntity.setPreparationTime(20);
    }

    @Test
    @DisplayName("Should create pizza successfully")
    void shouldCreatePizzaSuccessfully() {
        PizzaIngredient pizzaIngredient = pizza.getIngredients().get(0);
        PizzaIngredientEntity ingredientEntity = new PizzaIngredientEntity();

        Pizza expectedPizza =
                PizzaTestDataBuilder
                        .aPizza()
                        .withId(1L)
                        .build();

        PizzaIngredient expectedIngredient = new PizzaIngredient();

        expectedIngredient.setId(1L);
        expectedIngredient.setPizzaId(1L);
        expectedIngredient.setIngredientId(4L);
        expectedIngredient.setIngredientName("Pepperoni");
        expectedIngredient.setQuantity(new BigDecimal("0.25"));

        when(pizzaPersistenceMapper.toEntity(pizza)).thenReturn(pizzaEntity);
        when(pizzaJpaRepository.save(pizzaEntity)).thenReturn(pizzaEntity);
        when(pizzaIngredientPersistenceMapper.toEntity(pizzaIngredient))
                .thenReturn(ingredientEntity);
        when(pizzaPersistenceMapper.toDomain(pizzaEntity)).thenReturn(expectedPizza);
        when(pizzaIngredientJpaRepository.findByPizzaId(1L))
                .thenReturn(List.of(ingredientEntity));
        when(pizzaIngredientPersistenceMapper.toDomain(ingredientEntity))
                .thenReturn(expectedIngredient);

        Pizza result = adapter.createPizza(pizza);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getProductId());
        assertEquals(20, result.getPreparationTime());

        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());
        assertEquals(4L, result.getIngredients().get(0).getIngredientId());

        verify(pizzaPersistenceMapper).toEntity(pizza);
        verify(pizzaJpaRepository).save(pizzaEntity);
        verify(pizzaIngredientPersistenceMapper).toEntity(pizzaIngredient);
        verify(pizzaIngredientJpaRepository).save(ingredientEntity);
        verify(pizzaIngredientJpaRepository).findByPizzaId(1L);
        verify(pizzaIngredientPersistenceMapper).toDomain(ingredientEntity);
        verify(pizzaPersistenceMapper).toDomain(pizzaEntity);
    }

    @Test
    @DisplayName("Should create pizza without ingredients when list is null")
    void shouldCreatePizzaWithoutIngredientsWhenListIsNull() {
        Pizza pizzaWithoutIngredients =
                PizzaTestDataBuilder
                        .aPizza()
                        .withoutIngredients()
                        .build();

        when(pizzaPersistenceMapper.toEntity(pizzaWithoutIngredients)).thenReturn(pizzaEntity);
        when(pizzaJpaRepository.save(pizzaEntity)).thenReturn(pizzaEntity);

        Pizza expectedPizza =
                PizzaTestDataBuilder
                        .aPizza()
                        .withId(1L)
                        .withoutIngredients()
                        .build();

        when(pizzaPersistenceMapper.toDomain(pizzaEntity)).thenReturn(expectedPizza);
        when(pizzaIngredientJpaRepository.findByPizzaId(1L)).thenReturn(List.of());

        Pizza result = adapter.createPizza(pizzaWithoutIngredients);

        assertNotNull(result);
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());

        verify(pizzaJpaRepository).save(pizzaEntity);
        verify(pizzaIngredientJpaRepository).findByPizzaId(1L);

        verify(pizzaIngredientJpaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find pizza by product id successfully")
    void shouldFindPizzaByProductIdSuccessfully() {
        when(pizzaJpaRepository.findByProductIdAndDeletedAtIsNull(1L))
                .thenReturn(Optional.of(pizzaEntity));

        Pizza pizza = PizzaTestDataBuilder
                        .aPizza()
                        .withId(1L)
                        .build();

        when(pizzaPersistenceMapper.toDomain(pizzaEntity)).thenReturn(pizza);

        PizzaIngredientEntity ingredientEntity = new PizzaIngredientEntity();
        PizzaIngredient ingredient = new PizzaIngredient();

        ingredient.setId(1L);
        ingredient.setPizzaId(1L);
        ingredient.setIngredientId(4L);
        ingredient.setIngredientName("Pepperoni");
        ingredient.setQuantity(new BigDecimal("0.25"));

        when(pizzaIngredientJpaRepository.findByPizzaId(1L))
                .thenReturn(List.of(ingredientEntity));
        when(pizzaIngredientPersistenceMapper.toDomain(ingredientEntity))
                .thenReturn(ingredient);

        Optional<Pizza> result = adapter.findByProductId(1L);

        assertTrue(result.isPresent());

        Pizza foundPizza = result.get();

        assertEquals(1L, foundPizza.getId());
        assertEquals(1L, foundPizza.getProductId());
        assertNotNull(foundPizza.getIngredients());
        assertEquals(1, foundPizza.getIngredients().size());
        assertEquals(4L, foundPizza.getIngredients().get(0).getIngredientId());

        verify(pizzaJpaRepository).findByProductIdAndDeletedAtIsNull(1L);
        verify(pizzaPersistenceMapper).toDomain(pizzaEntity);
        verify(pizzaIngredientJpaRepository).findByPizzaId(1L);
        verify(pizzaIngredientPersistenceMapper).toDomain(ingredientEntity);
    }

    @Test
    @DisplayName("Should return empty when pizza does not exist by product id")
    void shouldReturnEmptyWhenPizzaDoesNotExistByProductId() {
        when(pizzaJpaRepository.findByProductIdAndDeletedAtIsNull(999L))
                .thenReturn(Optional.empty());

        Optional<Pizza> result = adapter.findByProductId(999L);

        assertTrue(result.isEmpty());

        verify(pizzaJpaRepository).findByProductIdAndDeletedAtIsNull(999L);

        verifyNoInteractions(pizzaPersistenceMapper);
        verifyNoInteractions(pizzaIngredientJpaRepository);
        verifyNoInteractions(pizzaIngredientPersistenceMapper);
    }

}