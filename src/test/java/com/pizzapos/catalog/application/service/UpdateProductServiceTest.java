package com.pizzapos.catalog.application.service;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import com.pizzapos.support.ProductTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateProductServiceTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    private UpdateProductService service;

    @BeforeEach
    void setUp() {
        service = new UpdateProductService(repositoryPort);
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductSuccessfully() {
        // Given
        Product existingProduct = ProductTestDataBuilder
                .aPizza()
                .build();

        existingProduct.setId(1L);

        Product request = ProductTestDataBuilder
                .aPizza()
                .withName("Mexicana")
                .build();

        request.setPrice(new BigDecimal("249.99"));

        when(repositoryPort.findById(1L))
                .thenReturn(Optional.of(existingProduct));
        when(repositoryPort.findByName("Mexicana"))
                .thenReturn(Optional.empty());
        when(repositoryPort.save(existingProduct))
                .thenReturn(existingProduct);

        // When
        Product result = service.updateProduct(1L, request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mexicana", result.getName());
        assertEquals(new BigDecimal("249.99"), result.getPrice());

        verify(repositoryPort).findById(1L);
        verify(repositoryPort).findByName("Mexicana");
        verify(repositoryPort).save(existingProduct);
    }

    @Test
    @DisplayName("Should throw exception when product does not exist")
    void shouldThrowExceptionWhenProductDoesNotExist() {
        // Given
        Product request = ProductTestDataBuilder
                .aPizza()
                .build();

        when(repositoryPort.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateProduct(999L, request)
        );

        assertEquals(Messages.PRODUCT_NOT_FOUND, exception.getMessage());

        verify(repositoryPort).findById(999L);
        verify(repositoryPort, never()).findByName(anyString());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when product name already exists")
    void shouldThrowExceptionWhenProductNameAlreadyExists() {
        // Given
        Product existingProduct = ProductTestDataBuilder
                .aPizza()
                .build();

        existingProduct.setId(1L);

        Product anotherProduct = ProductTestDataBuilder
                .aPizza()
                .withName("Mexicana")
                .build();

        anotherProduct.setId(2L);

        Product request = ProductTestDataBuilder
                .aPizza()
                .withName("Mexicana")
                .build();

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(repositoryPort.findByName("Mexicana")).thenReturn(Optional.of(anotherProduct));

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.updateProduct(1L, request)
        );
        assertEquals(Messages.PRODUCT_ALREADY_EXISTS, exception.getMessage());

        verify(repositoryPort).findById(1L);
        verify(repositoryPort).findByName("Mexicana");
        verify(repositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should allow update when product keeps its current name")
    void shouldAllowUpdateWhenProductKeepsItsCurrentName() {
        // Given
        Product existingProduct = ProductTestDataBuilder
                .aPizza()
                .build();

        existingProduct.setId(1L);
        existingProduct.setPrice(new BigDecimal("249.99"));

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(repositoryPort.findByName("Pepperoni")).thenReturn(Optional.of(existingProduct));
        when(repositoryPort.save(existingProduct)).thenReturn(existingProduct);

        // When
        Product result = service.updateProduct(1L, existingProduct);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pepperoni", result.getName());
        assertEquals(new BigDecimal("249.99"), result.getPrice());

        verify(repositoryPort).findById(1L);
        verify(repositoryPort).findByName("Pepperoni");
        verify(repositoryPort).save(existingProduct);
    }

}
