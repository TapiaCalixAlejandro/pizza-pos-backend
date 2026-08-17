package com.pizzapos.catalog.presentation.mapper;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.presentation.dto.request.CreateProductRequest;
import com.pizzapos.catalog.presentation.dto.request.UpdateProductRequest;
import com.pizzapos.catalog.presentation.dto.response.ProductResponse;
import com.pizzapos.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface ProductPresentationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productStatus", constant = "ACTIVE")
    Product toDomain(CreateProductRequest request);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productStatus", ignore = true)
    Product toDomain(UpdateProductRequest request);

}
