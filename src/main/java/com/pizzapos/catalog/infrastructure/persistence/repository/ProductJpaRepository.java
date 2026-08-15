package com.pizzapos.catalog.infrastructure.persistence.repository;

import com.pizzapos.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<ProductEntity> findByNameAndDeletedAtIsNull(String name);

    List<ProductEntity> findAllByDeletedAtIsNull();

    boolean existsByNameAndDeletedAtIsNull(String name);

}
