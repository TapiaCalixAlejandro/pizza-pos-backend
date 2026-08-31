package com.pizzapos.catalog.application.service.pizza;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.catalog.domain.ports.out.PizzaRepositoryPort;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import com.pizzapos.support.PizzaTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CreatePizzaServiceTest {

    @Mock
    private PizzaRepositoryPort pizzaRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private IngredientRepositoryPort ingredientRepositoryPort;

    private CreatePizzaService service;

    @BeforeEach
    void setUp() {
        service = new CreatePizzaService(
                pizzaRepositoryPort,
                productRepositoryPort,
                ingredientRepositoryPort
        );
    }

    @Test
    @DisplayName("Should create pizza successfully")
    void shouldCreatePizzaSuccessfully() {
        Pizza pizza = PizzaTestDataBuilder.aPizza().build();

        Pizza savedPizza = PizzaTestDataBuilder
                .aPizza()
                .withId(1L)
                .build();

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(new Product()));
        when(pizzaRepositoryPort.findByProductId(1L)).thenReturn(Optional.empty());
        when(ingredientRepositoryPort.findById(4L)).thenReturn(Optional.of(new Ingredient()));
        when(pizzaRepositoryPort.createPizza(pizza)).thenReturn(savedPizza);

        Pizza result = service.createPizza(pizza);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getProductId());
        assertEquals(20, result.getPreparationTime());

        verify(productRepositoryPort).findById(1L);
        verify(pizzaRepositoryPort).findByProductId(1L);
        verify(ingredientRepositoryPort).findById(4L);
        verify(pizzaRepositoryPort).createPizza(pizza);
    }

    @Test
    @DisplayName("Should throw exception when product does not exist")
    void shouldThrowExceptionWhenProductDoesNotExist() {
        Pizza pizza = PizzaTestDataBuilder.aPizza().build();

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.createPizza(pizza)
                );

        assertEquals(Messages.PRODUCT_NOT_FOUND, exception.getMessage());

        verify(productRepositoryPort).findById(1L);

        verifyNoInteractions(pizzaRepositoryPort);
        verifyNoInteractions(ingredientRepositoryPort);
    }

    @Test
    @DisplayName("Should throw exception when pizza already exists")
    void shouldThrowExceptionWhenPizzaAlreadyExists() {
        Pizza pizza = PizzaTestDataBuilder.aPizza().build();

        Pizza existingPizza = PizzaTestDataBuilder
                .aPizza()
                .withId(10L)
                .build();

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(new Product()));
        when(pizzaRepositoryPort.findByProductId(1L)).thenReturn(Optional.of(existingPizza));

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () -> service.createPizza(pizza)
                );

        assertEquals(Messages.PIZZA_ALREADY_EXISTS, exception.getMessage());

        verify(productRepositoryPort).findById(1L);
        verify(pizzaRepositoryPort).findByProductId(1L);

        verifyNoInteractions(ingredientRepositoryPort);
        verify(pizzaRepositoryPort, never()).createPizza(any());
    }

    @Test
    @DisplayName("Should throw exception when pizza has no ingredients")
    void shouldThrowExceptionWhenPizzaHasNoIngredients() {
        Pizza pizza = PizzaTestDataBuilder
                .aPizza()
                .withoutIngredients()
                .build();

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(new Product()));
        when(pizzaRepositoryPort.findByProductId(1L)).thenReturn(Optional.empty());

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () -> service.createPizza(pizza)
                );

        assertEquals(Messages.PIZZA_MUST_HAVE_INGREDIENTS, exception.getMessage());

        verify(productRepositoryPort).findById(1L);
        verify(pizzaRepositoryPort).findByProductId(1L);

        verifyNoInteractions(ingredientRepositoryPort);
        verify(pizzaRepositoryPort, never()).createPizza(pizza);
    }

    @Test
    @DisplayName("Should throw exception when ingredient does not exist")
    void shouldThrowExceptionWhenIngredientDoesNotExist() {
        Pizza pizza = PizzaTestDataBuilder.aPizza().build();

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(new Product()));
        when(pizzaRepositoryPort.findByProductId(1L)).thenReturn(Optional.empty());
        when(ingredientRepositoryPort.findById(4L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.createPizza(pizza)
                );

        assertEquals(Messages.INGREDIENT_NOT_FOUND, exception.getMessage());

        verify(productRepositoryPort).findById(1L);
        verify(pizzaRepositoryPort).findByProductId(1L);
        verify(ingredientRepositoryPort).findById(4L);
        verify(pizzaRepositoryPort, never()).createPizza(pizza);
    }

    @Test
    @DisplayName("Should validate all ingredients before creating pizza")
    void shouldValidateAllIngredientsBeforeCreatingPizza() {
        Pizza pizza = PizzaTestDataBuilder
                .aPizza()
                .withoutIngredients()
                .withIngredient(4L, new BigDecimal("0.250"))
                .withIngredient(5L, new BigDecimal("0.150"))
                .build();

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(new Product()));
        when(pizzaRepositoryPort.findByProductId(1L)).thenReturn(Optional.empty());
        when(ingredientRepositoryPort.findById(4L)).thenReturn(Optional.of(new Ingredient()));
        when(ingredientRepositoryPort.findById(5L)).thenReturn(Optional.of(new Ingredient()));
        when(pizzaRepositoryPort.createPizza(pizza)).thenReturn(pizza);

        Pizza result = service.createPizza(pizza);

        assertNotNull(result);

        verify(ingredientRepositoryPort).findById(4L);
        verify(ingredientRepositoryPort).findById(5L);
        verify(pizzaRepositoryPort).createPizza(pizza);
    }

    @Test
    @DisplayName("Should not create pizza when second ingredient does not exist")
    void shouldNotCreatePizzaWhenSecondIngredientDoesNotExist() {
        Pizza pizza = PizzaTestDataBuilder
                .aPizza()
                .withoutIngredients()
                .withIngredient(4L, new BigDecimal("0.250"))
                .withIngredient(999L, new BigDecimal("0.100"))
                .build();

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(new Product()));
        when(pizzaRepositoryPort.findByProductId(1L)).thenReturn(Optional.empty());
        when(ingredientRepositoryPort.findById(4L)).thenReturn(Optional.of(new Ingredient()));
        when(ingredientRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.createPizza(pizza)
                );

        assertEquals(Messages.INGREDIENT_NOT_FOUND, exception.getMessage());

        verify(pizzaRepositoryPort, never()).createPizza(any());
    }

}