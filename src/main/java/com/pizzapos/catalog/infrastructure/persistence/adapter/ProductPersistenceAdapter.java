package com.pizzapos.catalog.infrastructure.persistence.adapter;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pizzapos.catalog.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.repository.ProductJpaRepository;

import java.util.List;
import java.util.Optional;

public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final ProductPersistenceMapper productMapper;

    public ProductPersistenceAdapter(
            ProductJpaRepository productJpaRepository,
            ProductPersistenceMapper productMapper
    ) {
        this.productJpaRepository = productJpaRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity savedEntity = productJpaRepository.save(entity);

        return productMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {

        return productJpaRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public Optional<Product> findByName(String name) {

        return productJpaRepository.findByName(name)
                .map(productMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {

        return productJpaRepository.findAll()
                .stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {

        return productJpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

}
