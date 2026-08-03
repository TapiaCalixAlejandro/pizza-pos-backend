package com.pizzapos.catalog.application.service;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.in.CreateProductUseCase;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductService.class);

    public CreateProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        LOGGER.info(
                "Creating product [name={}, type={}, price={}]",
                product.getName(),
                product.getProductType(),
                product.getPrice()
        );

        validateProductName(product.getName());

        Product saved = productRepositoryPort.save(product);

        LOGGER.info(
                "Product created successfully [id={}, name={}]",
                saved.getId(),
                saved.getName()
        );

        return saved;
    }

    private void validateProductName(String productName) {

        productRepositoryPort.findByName(productName)
                .ifPresent(product -> {
                    throw new BusinessException(Messages.PRODUCT_ALREADY_EXISTS);
                });
    }

}
