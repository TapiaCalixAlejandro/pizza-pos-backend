package com.pizzapos.catalog.presentation.mapper;

import com.pizzapos.catalog.domain.enums.ProductStatus;
import com.pizzapos.catalog.domain.enums.ProductType;
import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.presentation.dto.response.ProductResponse;
import com.pizzapos.support.ProductTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductPresentationMapperTest {

    private final ProductPresentationMapper mapper =
            Mappers.getMapper(ProductPresentationMapper.class);

    @Test
    void shouldMapProductToProductResponse() {
        // Given
        Product product = ProductTestDataBuilder
                .aPizza()
                .build();

        // When
        ProductResponse response = mapper.toResponse(product);

        // Then
        assertNotNull(response);

        assertEquals(product.getId(), response.getId());
        assertEquals("Pepperoni", response.getName());
        assertEquals("Classic Pepperoni", response.getDescription());
        assertEquals(new BigDecimal("199.99"), response.getPrice());
        assertEquals("pepperoni.png", response.getImage());
        assertEquals(ProductType.PIZZA, response.getProductType());
        assertEquals(ProductStatus.ACTIVE, response.getProductStatus());
    }

    @Test
    void shouldMapProductListToProductResponseList() {
        // Given
        Product pepperoni = ProductTestDataBuilder
                .aPizza()
                .build();

        Product mexicana = ProductTestDataBuilder
                .aPizza()
                .withName("Mexicana")
                .build();

        // When
        List<ProductResponse> response =
                mapper.toResponseList(List.of(pepperoni, mexicana));

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals("Pepperoni", response.get(0).getName());
        assertEquals("Mexicana", response.get(1).getName());
    }

}
