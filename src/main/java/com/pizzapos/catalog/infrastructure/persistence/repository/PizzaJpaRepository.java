package com.pizzapos.catalog.infrastructure.persistence.repository;

import com.pizzapos.catalog.infrastructure.persistence.entity.PizzaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PizzaJpaRepository extends JpaRepository<PizzaEntity, Long> {

    Optional<PizzaEntity> findByProductIdAndDeletedAtIsNull(Long productId);

}
