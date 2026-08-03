package com.pizzapos.catalog.domain.ports.out;

import com.pizzapos.catalog.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findByName(String name);

    List<Product> findAll();

    boolean existsByName(String name);

    void deleteById(Long id);

}
