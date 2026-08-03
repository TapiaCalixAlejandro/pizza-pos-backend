package com.pizzapos.catalog.infrastructure.persistence.repository;

import com.pizzapos.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByName(String name);

    boolean existsByName(String name);

}
