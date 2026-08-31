package com.pizzapos.catalog.presentation.controller;

import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.ports.in.pizza.CreatePizzaUseCase;
import com.pizzapos.catalog.presentation.dto.pizza.request.CreatePizzaRequest;
import com.pizzapos.catalog.presentation.dto.pizza.response.PizzaResponse;
import com.pizzapos.catalog.presentation.mapper.PizzaPresentationMapper;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.response.ApiResponse;
import com.pizzapos.shared.response.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pizzas")
@Tag(
        name = "Pizzas",
        description = "Operations related to pizza management"
)
public class PizzaController {

    private final ResponseFactory responseFactory;
    private final CreatePizzaUseCase createPizzaUseCase;
    private final PizzaPresentationMapper pizzaPresentationMapper;

    public PizzaController(
            ResponseFactory responseFactory,
            CreatePizzaUseCase createPizzaUseCase,
            PizzaPresentationMapper pizzaPresentationMapper
    ) {
        this.responseFactory = responseFactory;
        this.createPizzaUseCase = createPizzaUseCase;
        this.pizzaPresentationMapper = pizzaPresentationMapper;
    }

    @Operation(
            summary = "Create a pizza",
            description = "Creates a pizza configuration for an existing product"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Pizza created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product or ingredient not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Pizza already exists"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<PizzaResponse>> createPizza(
            @Valid @RequestBody CreatePizzaRequest request
    ) {
        Pizza pizza = pizzaPresentationMapper.toDomain(request);
        Pizza saved = createPizzaUseCase.createPizza(pizza);
        PizzaResponse response = pizzaPresentationMapper.toResponse(saved);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        responseFactory.success(
                                Messages.PIZZA_CREATED,
                                response
                        )
                );
    }

}
