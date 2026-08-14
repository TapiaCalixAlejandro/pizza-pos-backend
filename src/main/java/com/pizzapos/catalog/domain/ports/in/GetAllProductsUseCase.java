package com.pizzapos.catalog.domain.ports.in;

import com.pizzapos.catalog.domain.model.Product;

import java.util.List;

public interface GetAllProductsUseCase {

    List<Product> getAllProducts();

}
