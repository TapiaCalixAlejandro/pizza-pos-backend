package com.pizzapos.catalog.infrastructure.persistence.adapter;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.catalog.infrastructure.persistence.entity.IngredientEntity;
import com.pizzapos.catalog.infrastructure.persistence.mapper.IngredientPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.repository.IngredientJpaRepository;

import java.util.List;
import java.util.Optional;

public class IngredientPersistenceAdapter implements IngredientRepositoryPort {

    private final IngredientJpaRepository ingredientJpaRepository;
    private final IngredientPersistenceMapper ingredientPersistenceMapper;

    public IngredientPersistenceAdapter(
            IngredientJpaRepository ingredientJpaRepository,
            IngredientPersistenceMapper ingredientPersistenceMapper
    ) {
        this.ingredientJpaRepository = ingredientJpaRepository;
        this.ingredientPersistenceMapper = ingredientPersistenceMapper;
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        IngredientEntity entity = ingredientPersistenceMapper.toEntity(ingredient);
        IngredientEntity savedEntity = ingredientJpaRepository.save(entity);

        return ingredientPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Ingredient> findById(Long id) {

        return ingredientJpaRepository
                .findByIdAndDeletedAtIsNull(id)
                .map(ingredientPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Ingredient> findByName(String name) {

        return ingredientJpaRepository
                .findByNameAndDeletedAtIsNull(name)
                .map(ingredientPersistenceMapper::toDomain);
    }

    @Override
    public List<Ingredient> findAll() {
        return List.of();
    }

    @Override
    public boolean existsByName(String name) {

        return ingredientJpaRepository
                .existsByNameAndDeletedAtIsNull(name);
    }

    @Override
    public void deleteById(Long id) {

    }
}
