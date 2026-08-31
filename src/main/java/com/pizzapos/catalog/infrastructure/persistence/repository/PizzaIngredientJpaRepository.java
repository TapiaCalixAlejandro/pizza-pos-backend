package com.pizzapos.catalog.infrastructure.persistence.repository;

import com.pizzapos.catalog.infrastructure.persistence.entity.PizzaIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaIngredientJpaRepository extends JpaRepository<PizzaIngredientEntity, Long> {

    List<PizzaIngredientEntity> findByPizzaId(Long pizzaId);

    void deleteByPizzaId(Long pizzaId);

}
