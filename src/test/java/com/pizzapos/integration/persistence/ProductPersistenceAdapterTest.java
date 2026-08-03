package com.pizzapos.integration.persistence;

import com.pizzapos.catalog.domain.enums.ProductStatus;
import com.pizzapos.catalog.domain.enums.ProductType;
import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.infrastructure.persistence.adapter.ProductPersistenceAdapter;
import com.pizzapos.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pizzapos.catalog.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.mapper.ProductPersistenceMapperImpl;
import com.pizzapos.catalog.infrastructure.persistence.repository.ProductJpaRepository;
import com.pizzapos.support.PersistenceTest;
import com.pizzapos.support.ProductTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
        ProductPersistenceAdapter.class,
        ProductPersistenceMapperImpl.class
})
public class ProductPersistenceAdapterTest extends PersistenceTest {

    @Autowired
    private ProductPersistenceAdapter adapter;

    @Autowired
    private ProductJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should save a product successfully")
    void shouldSaveProductSuccessfully() {
        // Given
        Product product = ProductTestDataBuilder
                .aPizza()
                .build();

        // When
        Product saved = adapter.save(product);

        // Then
        assertNotNull(saved);
        assertNotNull(saved.getId());

        assertEquals("Pepperoni", saved.getName());
        assertEquals("Classic Pepperoni", saved.getDescription());
        assertEquals(new BigDecimal("199.99"), saved.getPrice());
        assertEquals("pepperoni.png", saved.getImage());
        assertEquals(ProductStatus.ACTIVE, saved.getProductStatus());
        assertEquals(ProductType.PIZZA, saved.getProductType());

        assertEquals(1, repository.count());

        ProductEntity entity = repository.findAll().get(0);

        assertNotNull(entity.getId());
        assertEquals("Pepperoni", entity.getName());
        assertEquals("Classic Pepperoni", entity.getDescription());
        assertEquals(new BigDecimal("199.99"), entity.getPrice());
        assertEquals(ProductStatus.ACTIVE, entity.getProductStatus());
        assertEquals(ProductType.PIZZA, entity.getProductType());
    }

    @Test
    @DisplayName("Should find product by name")
    void shouldFindProductByName() {
        // Given
        Product product = ProductTestDataBuilder
                .aPizza()
                .build();

        adapter.save(product);

        // When
        Optional<Product> result = adapter.findByName("Pepperoni");

        // Then
        assertTrue(result.isPresent());

        Product found = result.get();

        assertEquals("Pepperoni", found.getName());
        assertEquals("Classic Pepperoni", found.getDescription());
        assertEquals(new BigDecimal("199.99"), found.getPrice());
        assertEquals(ProductType.PIZZA, found.getProductType());
        assertEquals(ProductStatus.ACTIVE, found.getProductStatus());
    }

    @Test
    @DisplayName("Should return empty when product does not exist")
    void shouldReturnEmptyWhenProductDoesNotExist() {
        // When
        Optional<Product> result = adapter.findByName("Mexicana");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return true when product exists")
    void shouldReturnTrueWhenProductExists() {
        // Given
        Product product = ProductTestDataBuilder
                .aPizza()
                .build();

        adapter.save(product);

        // When
        boolean exists = adapter.existsByName("Pepperoni");

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when product does not exist")
    void shouldReturnFalseWhenProductDoesNotExist() {
        // When
        boolean exists = adapter.existsByName("Mexican");

        // Then
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should find product by id")
    void shouldFindProductById() {
        // Given
        Product saved = adapter.save(ProductTestDataBuilder.aPizza().build());

        // When
        Optional<Product> result = adapter.findById(saved.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Should return all products")
    void shouldReturnAllProducts() {
        // Given
        adapter.save(ProductTestDataBuilder.aPizza().build());

        adapter.save(ProductTestDataBuilder.aPizza()
                        .withName("Mexicana")
                        .build()
        );

        // When
        List<Product> products = adapter.findAll();

        // Then
        assertEquals(2, products.size());
    }

    @Test
    @DisplayName("Should delete product by id")
    void shouldDeleteProductById() {
        // Given
        Product saved = adapter.save(ProductTestDataBuilder.aPizza().build());

        // When
        adapter.deleteById(saved.getId());

        // Then
        assertEquals(0, repository.count());
    }

}
