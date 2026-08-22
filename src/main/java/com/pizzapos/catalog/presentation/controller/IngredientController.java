package com.pizzapos.catalog.presentation.controller;

import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.in.ingredient.CreateIngredientUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.GetIngredientByIdUseCase;
import com.pizzapos.catalog.presentation.dto.ingredient.request.CreateIngredientRequest;
import com.pizzapos.catalog.presentation.dto.ingredient.response.IngredientResponse;
import com.pizzapos.catalog.presentation.mapper.IngredientPresentationMapper;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.response.ApiResponse;
import com.pizzapos.shared.response.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingredients")
@Tag(
        name = "Ingredients",
        description = "Operations related to ingredient management"
)
public class IngredientController {

    private final ResponseFactory responseFactory;
    private final CreateIngredientUseCase createIngredientUseCase;
    private final GetIngredientByIdUseCase getIngredientByIdUseCase;
    private final IngredientPresentationMapper ingredientPresentationMapper;

    public IngredientController(
            ResponseFactory responseFactory,
            CreateIngredientUseCase createIngredientUseCase,
            GetIngredientByIdUseCase getIngredientByIdUseCase,
            IngredientPresentationMapper ingredientPresentationMapper
    ) {
        this.responseFactory = responseFactory;
        this.createIngredientUseCase = createIngredientUseCase;
        this.getIngredientByIdUseCase = getIngredientByIdUseCase;
        this.ingredientPresentationMapper = ingredientPresentationMapper;
    }

    @Operation(
            summary = "Create a new ingredient",
            description = "Creates a new ingredient in the inventory"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Ingredient created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation Error"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Ingredient already exists"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<IngredientResponse>> createIngredient(
            @Valid @RequestBody CreateIngredientRequest request
    ) {
        Ingredient ingredient = ingredientPresentationMapper.toDomain(request);
        Ingredient savedIngredient = createIngredientUseCase.createIngredient(ingredient);
        IngredientResponse response = ingredientPresentationMapper.toResponse(savedIngredient);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseFactory.success(Messages.INGREDIENT_CREATED, response));
    }

    @Operation(
            summary = "Get ingredient by id",
            description = "Returns a ingredient by its identifier"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Ingredient found successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Ingredient not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IngredientResponse>> findIngredientById(
            @PathVariable Long id
    ) {
        Ingredient ingredient = getIngredientByIdUseCase.getIngredientById(id);
        IngredientResponse response = ingredientPresentationMapper.toResponse(ingredient);

        return ResponseEntity.ok(responseFactory.success(Messages.PRODUCT_FOUND, response));
    }

}
