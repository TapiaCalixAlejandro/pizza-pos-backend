package com.pizzapos.catalog.application.service;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.support.ProductTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllProductsServiceTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    private GetAllProductsService service;

    @BeforeEach
    void setUp() {
        service = new GetAllProductsService(repositoryPort);
    }

    @Test
    void shoulderReturnAllProducts() {
        // Given
        Product pepperoni = ProductTestDataBuilder
                .aPizza()
                .build();

        Product mexican = ProductTestDataBuilder
                .aPizza()
                .withName("Mexicana")
                .build();

        when(repositoryPort.findAll()).thenReturn(List.of(pepperoni, mexican));

        // When
        List<Product> result = service.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Pepperoni", result.get(0).getName());
        assertEquals("Mexicana", result.get(1).getName());

        verify(repositoryPort, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoProducts() {
        // Given
        when(repositoryPort.findAll()).thenReturn(List.of());

        // When
        List<Product> result = service.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(repositoryPort, times(1)).findAll();
    }

}
