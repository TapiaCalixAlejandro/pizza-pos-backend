package com.pizzapos.catalog.application.service.product;

import com.pizzapos.catalog.domain.enums.ProductStatus;
import com.pizzapos.catalog.domain.enums.ProductType;
import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateProductServiceTest {

    @Mock
    private ProductRepositoryPort repository;

    @InjectMocks
    private CreateProductService service;

    private Product buildProduct() {
        Product product = new Product();

        product.setName("Pepperoni");
        product.setDescription("Classic pepperoni pizza");
        product.setPrice(new BigDecimal("199.99"));
        product.setImage("pepperoni.png");
        product.setProductType(ProductType.PIZZA);
        product.setProductStatus(ProductStatus.ACTIVE);

        return product;
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // Given
        Product product = buildProduct();

        when(repository.findByName(product.getName())).thenReturn(Optional.empty());
        when(repository.save(product)).thenReturn(product);

        // When
        Product saved = service.createProduct(product);
        saved.setId(1L);

        // Then
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Pepperoni");
        assertThat(saved.getPrice()).isEqualByComparingTo("199.99");

        verify(repository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenProductAlreadyExists() {
        // Given
        Product product = buildProduct();

        when(repository.findByName(product.getName())).thenReturn(Optional.of(product));

        // When & Then
        assertThrows(BusinessException.class, () -> service.createProduct(product));

        verify(repository, never()).save(product);
    }

}
