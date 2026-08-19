package com.pizzapos.catalog.application.service.product;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.constants.Messages;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteProductByIdServiceTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    @InjectMocks
    private DeleteProductByIdService service;

    @Test
    @DisplayName("Should delete product successfully when product exists")
    void shouldDeleteProductSuccessfully() {
        // Given
        Product product = ProductTestDataBuilder
                .aPizza()
                .build();

        when(repositoryPort.findById(1L))
                .thenReturn(Optional.of(product));

        // When
        service.deleteProductById(1L);

        // Then
        verify(repositoryPort).findById(1L);
        verify(repositoryPort).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product does not exist")
    void shouldThrowExceptionWhenProductDoesNotExist() {
        // Given
        Long productId = 1L;

        when(repositoryPort.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteProductById(productId)
        );

        assertEquals(Messages.PRODUCT_NOT_FOUND + " ID: " + 1L, exception.getMessage());

        verify(repositoryPort).findById(productId);
        verify(repositoryPort, never()).deleteById(productId);
    }

}
