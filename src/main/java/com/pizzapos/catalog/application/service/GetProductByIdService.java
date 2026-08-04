package com.pizzapos.catalog.application.service;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.in.GetProductByIdUseCase;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.exception.ResourceNotFoundException;

public class GetProductByIdService implements GetProductByIdUseCase {

    private final ProductRepositoryPort repositoryPort;

    public GetProductByIdService(ProductRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Product getProductById(Long id) {

        return repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

}
