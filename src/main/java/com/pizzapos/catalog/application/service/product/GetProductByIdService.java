package com.pizzapos.catalog.application.service.product;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.in.product.GetProductByIdUseCase;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.ResourceNotFoundException;

public class GetProductByIdService implements GetProductByIdUseCase {

    private final ProductRepositoryPort repositoryPort;

    public GetProductByIdService(ProductRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Product getProductById(Long id) {

        return repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.PRODUCT_NOT_FOUND + " ID: " + id));
    }

}
