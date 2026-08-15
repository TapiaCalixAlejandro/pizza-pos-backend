package com.pizzapos.catalog.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzapos.catalog.domain.enums.ProductType;
import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.in.CreateProductUseCase;
import com.pizzapos.catalog.domain.ports.in.DeleteProductByIdUseCase;
import com.pizzapos.catalog.domain.ports.in.GetAllProductsUseCase;
import com.pizzapos.catalog.domain.ports.in.GetProductByIdUseCase;
import com.pizzapos.catalog.presentation.dto.request.CreateProductRequest;
import com.pizzapos.catalog.presentation.dto.response.ProductResponse;
import com.pizzapos.catalog.presentation.mapper.ProductPresentationMapper;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import com.pizzapos.shared.response.ApiErrorResponse;
import com.pizzapos.shared.response.ApiResponse;
import com.pizzapos.shared.response.ResponseFactory;
import com.pizzapos.support.ProductTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateProductUseCase createProductUseCase;

    @MockitoBean
    private GetProductByIdUseCase getProductByIdUseCase;

    @MockitoBean
    private GetAllProductsUseCase getAllProductsUseCase;

    @MockitoBean
    private DeleteProductByIdUseCase deleteProductByIdUseCase;

    @MockitoBean
    private ProductPresentationMapper productPresentationMapper;

    @MockitoBean
    private ResponseFactory responseFactory;

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() throws Exception {
        // Given
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Pepperoni");
        request.setDescription("Classic Pepperoni");
        request.setPrice(new BigDecimal("199.99"));
        request.setImage("pepperoni.png");
        request.setProductType(ProductType.PIZZA);

        Product product = ProductTestDataBuilder
                .aPizza()
                .build();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Pepperoni");
        productResponse.setDescription("Classic Pepperoni");
        productResponse.setPrice(new BigDecimal("199.99"));

        when(productPresentationMapper.toDomain(any(CreateProductRequest.class)))
                .thenReturn(product);

        when(createProductUseCase.createProduct(product))
                .thenReturn(product);

        when(productPresentationMapper.toResponse(product))
                .thenReturn(productResponse);

        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>(
                true,
                "Product created successfully",
                "test-trace-id",
                productResponse,
                null
        );

        when(responseFactory.success(
                anyString(),
                any(ProductResponse.class)
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(
                post("/api/v1/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andExpect(jsonPath("$.traceId").value("test-trace-id"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Pepperoni"))
                .andExpect(jsonPath("$.data.description").value("Classic Pepperoni"))
                .andExpect(jsonPath("$.data.price").value(199.99));

        verify(productPresentationMapper, times(1))
                .toDomain(any(CreateProductRequest.class));

        verify(createProductUseCase, times(1))
                .createProduct(product);

        verify(productPresentationMapper, times(1))
                .toResponse(product);

        verify(responseFactory, times(1))
                .success(anyString(), eq(productResponse));
    }

    @Test
    @DisplayName("Should return 400 when product type is missing")
    void shouldReturnBadRequestWhenProductTypeIsMissing() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Pepperoni");
        request.setDescription("Classic Pepperoni");
        request.setPrice(new BigDecimal("199.99"));
        request.setImage("Pepperoni.png");

        mockMvc.perform(
                post("/api/v1/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return all products successfully")
    void shouldReturnAllProductsSuccessfully() throws Exception {
        // Given
        Product pepperoni = new Product();
        pepperoni.setId(1L);
        pepperoni.setName("Pepperoni");
        pepperoni.setDescription("Classic Pepperoni");
        pepperoni.setPrice(new BigDecimal("199.99"));

        ProductResponse pepperoniResponse = new ProductResponse();
        pepperoniResponse.setId(1L);
        pepperoniResponse.setName("Pepperoni");
        pepperoniResponse.setDescription("Classic Pepperoni");
        pepperoniResponse.setPrice(new BigDecimal("199.99"));

        when(getAllProductsUseCase.getAllProducts())
                .thenReturn(List.of(pepperoni));

        when(productPresentationMapper.toResponseList(List.of(pepperoni)))
                .thenReturn(List.of(pepperoniResponse));

        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>(
                true,
                "Products retrieved successfully",
                "test-trace-id",
                List.of(pepperoniResponse),
                null
        );

        when(responseFactory.<List<ProductResponse>>success(
                anyString(),
                anyList()
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.traceId").value("test-trace-id"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Pepperoni"))
                .andExpect(jsonPath("$.data[0].description").value("Classic Pepperoni"))
                .andExpect(jsonPath("$.data[0].price").value(199.99));

        verify(getAllProductsUseCase, times(1))
                .getAllProducts();

        verify(productPresentationMapper, times(1))
                .toResponseList(List.of(pepperoni));

        verify(responseFactory, times(1))
                .success(anyString(), eq(List.of(pepperoniResponse)));
    }

    @Test
    @DisplayName("Should return empty list when there are no products")
    void shouldReturnEmptyListWhenThereAreNoProducts() throws Exception {
        // Given
        when(getAllProductsUseCase.getAllProducts())
                .thenReturn(List.of());

        when(productPresentationMapper.toResponseList(List.of()))
                .thenReturn(List.of());

        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>(
                true,
                "Product retrieved successfully",
                "test-trace-id",
                List.of(),
                null
        );

        when(responseFactory.<List<ProductResponse>>success(
                anyString(),
                anyList()
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(getAllProductsUseCase, times(1))
                .getAllProducts();

        verify(productPresentationMapper, times(1))
                .toResponseList(List.of());
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProductSuccessfully() throws Exception {
        // Given
        Long productId = 1L;

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                true,
                "Product deleted successfully",
                "test-trace-id",
                null,
                null
        );

        doNothing()
                .when(deleteProductByIdUseCase)
                .deleteProductById(productId);

        when(responseFactory.<Void>success(
                anyString(),
                isNull()
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(
                delete("/api/v1/products/{id}", productId)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product deleted successfully"))
                .andExpect(jsonPath("$.traceId").value("test-trace-id"));

        verify(deleteProductByIdUseCase).deleteProductById(productId);
        verify(responseFactory).success(anyString(), isNull());
    }

    @Test
    @DisplayName("Should return 404 when product does not exist")
    void shouldReturnNotFoundWhenProductDoesNotExist()  throws Exception {
        // Given
        Long productId = 999L;

        ResourceNotFoundException exception =
                new ResourceNotFoundException(Messages.PRODUCT_NOT_FOUND);

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        doThrow(exception)
                .when(deleteProductByIdUseCase)
                .deleteProductById(productId);

        when(responseFactory.error(
                HttpStatus.NOT_FOUND,
                Messages.PRODUCT_NOT_FOUND
        )).thenReturn(errorResponse);

        // When & Then
        mockMvc.perform(
                delete("/api/v1/products/{id}", productId)
        )
                .andExpect(status().isNotFound());

        verify(deleteProductByIdUseCase).deleteProductById(productId);
        verify(responseFactory)
                .error(
                        HttpStatus.NOT_FOUND,
                        Messages.PRODUCT_NOT_FOUND
                );
    }

}
