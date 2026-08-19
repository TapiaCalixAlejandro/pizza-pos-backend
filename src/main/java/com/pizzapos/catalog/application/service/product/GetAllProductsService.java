package com.pizzapos.catalog.application.service.product;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.in.product.GetAllProductsUseCase;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;

import java.util.List;

public class GetAllProductsService implements GetAllProductsUseCase {
    private final ProductRepositoryPort repositoryPort;

    public GetAllProductsService(ProductRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public List<Product> getAllProducts() {
        return repositoryPort.findAll();
    }

}
