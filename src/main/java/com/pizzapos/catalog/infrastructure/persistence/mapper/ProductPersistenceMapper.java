package com.pizzapos.catalog.infrastructure.persistence.mapper;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.infrastructure.persistence.entity.ProductEntity;
import com.pizzapos.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface ProductPersistenceMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ProductEntity toEntity(Product product);

    Product toDomain(ProductEntity entity);

    List<Product> toDomainList(List<ProductEntity> entities);

    List<ProductEntity> toEntityList(List<Product> products);

}
