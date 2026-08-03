package com.pizzapos.catalog.presentation.mapper;

import com.pizzapos.catalog.domain.enums.ProductStatus;
import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.presentation.dto.request.CreateProductRequest;
import com.pizzapos.catalog.presentation.dto.response.CreateProductResponse;
import com.pizzapos.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface ProductPresentationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productStatus", constant = "ACTIVE")
    Product toDomain(CreateProductRequest request);

    CreateProductResponse toResponse(Product product);

}
