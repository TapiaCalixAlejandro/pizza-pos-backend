package com.pizzapos.catalog.infrastructure.persistence.repository;

import com.pizzapos.catalog.infrastructure.persistence.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientJpaRepository extends JpaRepository<IngredientEntity, Long> {

    Optional<IngredientEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<IngredientEntity> findByNameAndDeletedAtIsNull(String name);

    boolean existsByNameAndDeletedAtIsNull(String name);

    List<IngredientEntity> findAllByDeletedAtIsNull();

}
