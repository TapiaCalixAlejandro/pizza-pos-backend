package com.pizzapos.catalog.presentation.controller;

import com.pizzapos.catalog.domain.model.Product;
import com.pizzapos.catalog.domain.ports.in.*;
import com.pizzapos.catalog.presentation.dto.request.CreateProductRequest;
import com.pizzapos.catalog.presentation.dto.request.UpdateProductRequest;
import com.pizzapos.catalog.presentation.dto.response.ProductResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(
        name = "Products",
        description = "Operations related to product management"
)
public class ProductController {

    private final ResponseFactory responseFactory;
    private final UpdateProductUseCase updateProductUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final DeleteProductByIdUseCase deleteProductByIdUseCase;
    private final ProductPresentationMapper productPresentationMapper;

    public ProductController(
            ResponseFactory responseFactory,
            UpdateProductUseCase updateProductUseCase,
            CreateProductUseCase createProductUseCase,
            GetProductByIdUseCase getProductByIdUseCase,
            GetAllProductsUseCase getAllProductsUseCase,
            DeleteProductByIdUseCase deleteProductByIdUseCase,
            ProductPresentationMapper productPresentationMapper
    ) {
        this.responseFactory = responseFactory;
        this.updateProductUseCase = updateProductUseCase;
        this.createProductUseCase = createProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.deleteProductByIdUseCase = deleteProductByIdUseCase;
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
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        Product product = productPresentationMapper.toDomain(request);
        Product savedProduct = createProductUseCase.createProduct(product);
        ProductResponse response = productPresentationMapper.toResponse(savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseFactory.success(Messages.PRODUCT_CREATED, response));
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
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long id
    ) {
        Product product = getProductByIdUseCase.getProductById(id);
        ProductResponse response = productPresentationMapper.toResponse(product);

        return ResponseEntity.ok(responseFactory.success(Messages.PRODUCT_FOUND, response));
    }

    @Operation(
            summary = "Get all products",
            description = "Returns all products available in the catalog"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<Product> products = getAllProductsUseCase.getAllProducts();
        List<ProductResponse> response = productPresentationMapper.toResponseList(products);

        return ResponseEntity.ok(responseFactory.success(Messages.PRODUCTS_RETRIEVED, response));
    }

    @Operation(
            summary = "Delete product by id",
            description = "Soft deletes a product by its identifier"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductById(
            @PathVariable Long id
    ) {
        deleteProductByIdUseCase.deleteProductById(id);

        return ResponseEntity.ok(responseFactory.success(Messages.PRODUCT_DELETED, null));
    }

    @Operation(
            summary = "Update product by id",
            description = "Updates an existing product in the catalog"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product update successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Product already exists"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        Product product = productPresentationMapper.toDomain(request);
        Product updatedProduct = updateProductUseCase.updateProduct(id, product);
        ProductResponse response = productPresentationMapper.toResponse(updatedProduct);

        return ResponseEntity.ok(
                responseFactory.success(
                        Messages.PRODUCT_UPDATED,
                        response
                )
        );
    }

}
