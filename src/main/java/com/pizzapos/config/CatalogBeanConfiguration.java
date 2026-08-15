package com.pizzapos.config;

import com.pizzapos.catalog.application.service.CreateProductService;
import com.pizzapos.catalog.application.service.DeleteProductByIdService;
import com.pizzapos.catalog.application.service.GetAllProductsService;
import com.pizzapos.catalog.application.service.GetProductByIdService;
import com.pizzapos.catalog.domain.ports.in.CreateProductUseCase;
import com.pizzapos.catalog.domain.ports.in.DeleteProductByIdUseCase;
import com.pizzapos.catalog.domain.ports.in.GetAllProductsUseCase;
import com.pizzapos.catalog.domain.ports.in.GetProductByIdUseCase;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.catalog.infrastructure.persistence.adapter.ProductPersistenceAdapter;
import com.pizzapos.catalog.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogBeanConfiguration {

    @Bean
    public ProductRepositoryPort productRepositoryPort(
            ProductJpaRepository repository,
            ProductPersistenceMapper mapper) {

        return new ProductPersistenceAdapter(repository, mapper);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(
            ProductRepositoryPort repositoryPort) {

        return new CreateProductService(repositoryPort);
    }

    @Bean
    public GetProductByIdUseCase getProductByIdUseCase(
            ProductRepositoryPort repositoryPort) {

        return new GetProductByIdService(repositoryPort);
    }

    @Bean
    public GetAllProductsUseCase getAllProductsUseCase(
            ProductRepositoryPort repositoryPort) {

        return new GetAllProductsService(repositoryPort);
    }

    @Bean
    public DeleteProductByIdUseCase deleteProductByIdUseCase(
            ProductRepositoryPort repositoryPort) {

        return new DeleteProductByIdService(repositoryPort);
    }

}
