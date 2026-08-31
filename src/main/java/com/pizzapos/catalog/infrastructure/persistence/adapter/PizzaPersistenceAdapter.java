package com.pizzapos.catalog.infrastructure.persistence.adapter;

import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.model.PizzaIngredient;
import com.pizzapos.catalog.domain.ports.out.PizzaRepositoryPort;
import com.pizzapos.catalog.infrastructure.persistence.entity.PizzaEntity;
import com.pizzapos.catalog.infrastructure.persistence.entity.PizzaIngredientEntity;
import com.pizzapos.catalog.infrastructure.persistence.mapper.PizzaIngredientPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.mapper.PizzaPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.repository.PizzaIngredientJpaRepository;
import com.pizzapos.catalog.infrastructure.persistence.repository.PizzaJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PizzaPersistenceAdapter implements PizzaRepositoryPort {

    private final PizzaJpaRepository pizzaJpaRepository;
    private final PizzaPersistenceMapper pizzaPersistenceMapper;
    private final PizzaIngredientJpaRepository pizzaIngredientJpaRepository;
    private final PizzaIngredientPersistenceMapper pizzaIngredientPersistenceMapper;

    public PizzaPersistenceAdapter(
            PizzaJpaRepository pizzaJpaRepository,
            PizzaPersistenceMapper pizzaPersistenceMapper,
            PizzaIngredientJpaRepository pizzaIngredientJpaRepository,
            PizzaIngredientPersistenceMapper pizzaIngredientPersistenceMapper
    ) {
        this.pizzaJpaRepository = pizzaJpaRepository;
        this.pizzaPersistenceMapper = pizzaPersistenceMapper;
        this.pizzaIngredientJpaRepository = pizzaIngredientJpaRepository;
        this.pizzaIngredientPersistenceMapper = pizzaIngredientPersistenceMapper;
    }

    @Override
    @Transactional
    public Pizza createPizza(Pizza pizza) {
        PizzaEntity pizzaEntity = pizzaPersistenceMapper.toEntity(pizza);
        PizzaEntity savedPizza = pizzaJpaRepository.save(pizzaEntity);

        if (pizza.getIngredients() != null) {
            for (PizzaIngredient ingredient : pizza.getIngredients()) {
                PizzaIngredientEntity ingredientEntity =
                        pizzaIngredientPersistenceMapper.toEntity(ingredient);

                ingredientEntity.setPizza(savedPizza);

                pizzaIngredientJpaRepository.save(ingredientEntity);
            }
        }

        Pizza result = pizzaPersistenceMapper.toDomain(savedPizza);

        List<PizzaIngredient> ingredients =
                pizzaIngredientJpaRepository
                        .findByPizzaId(savedPizza.getId())
                        .stream()
                        .map(pizzaIngredientPersistenceMapper::toDomain)
                        .toList();

        result.setIngredients(ingredients);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pizza> findByProductId(Long productId) {

        return pizzaJpaRepository
                .findByProductIdAndDeletedAtIsNull(productId)
                .map(entity -> {
                    Pizza pizza = pizzaPersistenceMapper.toDomain(entity);

                    List<PizzaIngredient> ingredients =
                            pizzaIngredientJpaRepository
                                    .findByPizzaId(entity.getId())
                                    .stream()
                                    .map(pizzaIngredientPersistenceMapper::toDomain)
                                    .toList();

                    pizza.setIngredients(ingredients);

                    return pizza;
                });
    }

}
