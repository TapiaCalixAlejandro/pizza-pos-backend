package com.pizzapos.catalog.application.service.product;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.in.product.UpdateProductUseCase;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateProductService.class);

    public UpdateProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product request) {
        LOGGER.info(
                "Update product [name={}, type={}, price={}]",
                request.getName(),
                request.getProductType(),
                request.getPrice()
        );

        Product existingProduct = productRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Messages.PRODUCT_NOT_FOUND)
                );

        Optional<Product> productWithSameName =
                productRepositoryPort.findByName(request.getName());

        if (productWithSameName.isPresent() && !productWithSameName.get().getId().equals(id)) {
            throw new BusinessException(Messages.PRODUCT_ALREADY_EXISTS);
        }

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setImage(request.getImage());
        existingProduct.setProductType(request.getProductType());

        LOGGER.info(
                "Product updated successfully [id={}, name={}]",
                existingProduct.getId(),
                existingProduct.getName()
        );

        return productRepositoryPort.save(existingProduct);
    }
}
