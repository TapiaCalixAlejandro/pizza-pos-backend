package com.pizzapos.config;

import com.pizzapos.catalog.application.service.ingredient.CreateIngredientService;
import com.pizzapos.catalog.application.service.ingredient.DeleteIngredientService;
import com.pizzapos.catalog.application.service.ingredient.GetAllIngredientsService;
import com.pizzapos.catalog.application.service.ingredient.GetIngredientByIdService;
import com.pizzapos.catalog.application.service.ingredient.UpdateIngredientService;
import com.pizzapos.catalog.application.service.product.*;
import com.pizzapos.catalog.domain.ports.in.ingredient.CreateIngredientUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.DeleteIngredientUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.GetAllIngredientsUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.GetIngredientByIdUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.UpdateIngredientUseCase;
import com.pizzapos.catalog.domain.ports.in.product.*;
import com.pizzapos.catalog.domain.ports.out.IngredientRepositoryPort;
import com.pizzapos.catalog.domain.ports.out.ProductRepositoryPort;
import com.pizzapos.catalog.infrastructure.persistence.adapter.IngredientPersistenceAdapter;
import com.pizzapos.catalog.infrastructure.persistence.adapter.ProductPersistenceAdapter;
import com.pizzapos.catalog.infrastructure.persistence.mapper.IngredientPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.pizzapos.catalog.infrastructure.persistence.repository.IngredientJpaRepository;
import com.pizzapos.catalog.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogBeanConfiguration {

    @Bean
    public ProductRepositoryPort productRepositoryPort(
            ProductJpaRepository productJpaRepository,
            ProductPersistenceMapper productPersistenceMapper
    ) {

        return new ProductPersistenceAdapter(productJpaRepository, productPersistenceMapper);
    }

    @Bean
    public IngredientRepositoryPort ingredientRepositoryPort(
            IngredientJpaRepository ingredientJpaRepository,
            IngredientPersistenceMapper ingredientPersistenceMapper
    ) {

        return new IngredientPersistenceAdapter(
                ingredientJpaRepository,
                ingredientPersistenceMapper
        );
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

    @Bean
    public UpdateProductUseCase updateProductUseCase(
            ProductRepositoryPort repositoryPort) {

        return new UpdateProductService(repositoryPort);
    }

    @Bean
    public CreateIngredientUseCase createIngredientUseCase(
            IngredientRepositoryPort ingredientRepositoryPort) {

        return new CreateIngredientService(ingredientRepositoryPort);
    }

    @Bean
    public GetIngredientByIdUseCase getIngredientByIdUseCase(
            IngredientRepositoryPort ingredientRepositoryPort) {

        return new GetIngredientByIdService(ingredientRepositoryPort);
    }

    @Bean
    public GetAllIngredientsUseCase getAllIngredientsUseCase(
            IngredientRepositoryPort ingredientRepositoryPort) {

        return new GetAllIngredientsService(ingredientRepositoryPort);
    }

    @Bean
    public DeleteIngredientUseCase deleteIngredientUseCase(
            IngredientRepositoryPort ingredientRepositoryPort) {

        return new DeleteIngredientService(ingredientRepositoryPort);
    }

    @Bean
    public UpdateIngredientUseCase updateIngredientUseCase(
            IngredientRepositoryPort ingredientRepositoryPort) {

        return new UpdateIngredientService(ingredientRepositoryPort);
    }

}
