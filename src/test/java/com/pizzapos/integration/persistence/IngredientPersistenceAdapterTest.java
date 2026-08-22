package com.pizzapos.integration.persistence;

import com.pizzapos.catalog.domain.enums.IngredientStatus;
import com.pizzapos.catalog.domain.enums.IngredientUnit;
import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.infrastructure.persistence.adapter.IngredientPersistenceAdapter;
import com.pizzapos.catalog.infrastructure.persistence.entity.IngredientEntity;
import com.pizzapos.catalog.infrastructure.persistence.mapper.IngredientPersistenceMapperImpl;
import com.pizzapos.catalog.infrastructure.persistence.repository.IngredientJpaRepository;
import com.pizzapos.support.IngredientTestDataBuilder;
import com.pizzapos.support.PersistenceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({
        IngredientPersistenceAdapter.class,
        IngredientPersistenceMapperImpl.class
})
public class IngredientPersistenceAdapterTest extends PersistenceTest {

    @Autowired
    private IngredientPersistenceAdapter persistenceAdapter;

    @Autowired
    private IngredientJpaRepository jpaRepository;

    @BeforeEach
    void setUp() {
        jpaRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save ingredient successfully")
    void shouldSaveIngredientSuccessfully() {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        // When
        Ingredient saved = persistenceAdapter.save(ingredient);

        // Then
        assertNotNull(saved);
        assertNotNull(saved.getId());

        assertEquals("Mozzarella", saved.getName());
        assertEquals(IngredientUnit.KILOGRAM, saved.getUnit());
        assertEquals(new BigDecimal("20"), saved.getStock());
        assertEquals(new BigDecimal("5"), saved.getMinimumStock());
        assertEquals(new BigDecimal("180.00"), saved.getCost());
        assertEquals(IngredientStatus.ACTIVE, saved.getStatus());

        assertEquals(1, jpaRepository.count());

        IngredientEntity entity = jpaRepository.findAll().get(0);

        assertNotNull(entity.getId());
        assertEquals("Mozzarella", entity.getName());
        assertEquals(IngredientUnit.KILOGRAM, entity.getUnit());
        assertEquals(new BigDecimal("20"), entity.getStock());
        assertEquals(new BigDecimal("5"), entity.getMinimumStock());
        assertEquals(new BigDecimal("180.00"), entity.getCost());
        assertEquals(IngredientStatus.ACTIVE, entity.getStatus());
    }

    @Test
    @DisplayName("Should find ingredient by name")
    void shouldFindIngredientByName() {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        persistenceAdapter.save(ingredient);

        // When
        Optional<Ingredient> result = persistenceAdapter.findByName("Mozzarella");

        // Then
        assertTrue(result.isPresent());

        Ingredient found = result.get();

        assertEquals("Mozzarella", found.getName());
        assertEquals(IngredientUnit.KILOGRAM, found.getUnit());
        assertEquals(new BigDecimal("20"), found.getStock());
        assertEquals(new BigDecimal("5"), found.getMinimumStock());
        assertEquals(new BigDecimal("180.00"), found.getCost());
        assertEquals(IngredientStatus.ACTIVE, found.getStatus());
    }

    @Test
    @DisplayName("Should return empty when ingredient does not exist")
    void shouldReturnEmptyWhenIngredientDoesNotExist() {
        // When
        Optional<Ingredient> result = persistenceAdapter.findByName("Mass");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return true when ingredient exists")
    void shouldReturnTrueWhenIngredientExists() {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        persistenceAdapter.save(ingredient);

        // When
        boolean exists = persistenceAdapter.existsByName("Mozzarella");

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when ingredient not exists")
    void shouldReturnFalseWhenIngredientNotExists() {
        // When
        boolean exists = persistenceAdapter.existsByName("Mass");

        // Then
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should find ingredient by id")
    void shouldFindIngredientById() {
        // Given
        Ingredient saved = persistenceAdapter.save(IngredientTestDataBuilder.anIngredient().build());

        // When
        Optional<Ingredient> result = persistenceAdapter.findById(saved.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

}
