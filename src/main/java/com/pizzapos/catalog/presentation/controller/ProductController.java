package com.pizzapos.catalog.presentation.controller;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.in.CreateProductUseCase;
import com.pizzapos.catalog.domain.ports.in.GetProductByIdUseCase;
import com.pizzapos.catalog.presentation.dto.request.CreateProductRequest;
import com.pizzapos.catalog.presentation.dto.response.CreateProductResponse;
import com.pizzapos.catalog.presentation.dto.response.GetProductResponse;
import com.pizzapos.catalog.presentation.mapper.ProductPresentationMapper;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.response.ApiResponse;
import com.pizzapos.shared.response.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Operations related to product management")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final ProductPresentationMapper productPresentationMapper;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductByIdUseCase getProductByIdUseCase,
            ProductPresentationMapper productPresentationMapper
    ) {
        this.createProductUseCase = createProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
        this.productPresentationMapper = productPresentationMapper;
    }

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product in the catalog"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Product already exists"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CreateProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        Product product = productPresentationMapper.toDomain(request);
        Product savedProduct = createProductUseCase.createProduct(product);
        CreateProductResponse response = productPresentationMapper.toCreateResponse(savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseFactory.success(Messages.PRODUCT_CREATED, response));
    }

    @Operation(
            summary = "Get product by id",
            description = "Returns a product by its identifier"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product found successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetProductResponse>> getProduct(
            @PathVariable Long id
    ) {
        Product product = getProductByIdUseCase.getProductById(id);
        GetProductResponse response = productPresentationMapper.toGetResponse(product);

        return ResponseEntity.ok(ResponseFactory.success(Messages.PRODUCT_FOUND, response));
    }

}
