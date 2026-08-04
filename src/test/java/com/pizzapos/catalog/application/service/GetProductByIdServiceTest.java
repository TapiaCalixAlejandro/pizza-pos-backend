package com.pizzapos.catalog.application.service;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import com.pizzapos.support.ProductTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetProductByIdServiceTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    @InjectMocks
    private GetProductByIdService byIdService;

    @Test
    @DisplayName("")
    void shouldReturnProductWhenProductExists() {
        // Given
        Product product = ProductTestDataBuilder.aPizza().build();

        when(repositoryPort.findById(1L))
                .thenReturn(Optional.of(product));

        // When
        Product result = byIdService.getProductById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Pepperoni", result.getName());

        verify(repositoryPort).findById(1L);
    }

    @Test
    @DisplayName("")
    void shouldThrowExceptionWhenProductDoesNotExists() {
        // Given
        when(repositoryPort.findById(1L))
                .thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> byIdService.getProductById(1L));

        assertEquals("Product not found with id: 1", exception.getMessage());

        verify(repositoryPort).findById(1L);
    }

}
