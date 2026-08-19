package com.pizzapos.catalog.domain.ports.in.product;

import com.pizzapos.catalog.domain.model.Product;

public interface CreateProductUseCase {

    Product createProduct(Product request);

}
