package com.pizzapos.catalog.domain.ports.out;

import com.pizzapos.catalog.domain.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepositoryPort {

    Ingredient save(Ingredient ingredient);

    Optional<Ingredient> findById(Long id);

    Optional<Ingredient> findByName(String name);

    List<Ingredient> findAll();

    boolean existsByName(String name);

    void deleteById(Long id);

}
