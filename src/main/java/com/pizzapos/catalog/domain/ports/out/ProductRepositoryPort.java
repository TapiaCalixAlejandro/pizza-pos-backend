package com.pizzapos.catalog.domain.ports.out;

import com.pizzapos.catalog.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findByName(String name);

    boolean existsByName(String name);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);

}
