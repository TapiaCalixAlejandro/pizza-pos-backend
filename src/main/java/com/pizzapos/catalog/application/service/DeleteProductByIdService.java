package com.pizzapos.catalog.application.service;

import com.pizzapos.catalog.domain.ports.in.DeleteProductByIdUseCase;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.ResourceNotFoundException;

public class DeleteProductByIdService implements DeleteProductByIdUseCase {

    private final ProductRepositoryPort repositoryPort;

    public DeleteProductByIdService(ProductRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public void deleteProductById(Long id) {

        repositoryPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Messages.PRODUCT_NOT_FOUND + " ID: " + id)
                );

        repositoryPort.deleteById(id);
    }

}
