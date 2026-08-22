package com.pizzapos.catalog.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzapos.catalog.domain.enums.IngredientStatus;
import com.pizzapos.catalog.domain.enums.IngredientUnit;
import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.in.ingredient.CreateIngredientUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.GetIngredientByIdUseCase;
import com.pizzapos.catalog.presentation.dto.ingredient.request.CreateIngredientRequest;
import com.pizzapos.catalog.presentation.dto.ingredient.response.IngredientResponse;
import com.pizzapos.catalog.presentation.mapper.IngredientPresentationMapper;
import com.pizzapos.shared.constants.Messages;
import com.pizzapos.shared.exception.BusinessException;
import com.pizzapos.shared.exception.ResourceNotFoundException;
import com.pizzapos.shared.response.ApiErrorResponse;
import com.pizzapos.shared.response.ApiResponse;
import com.pizzapos.shared.response.ResponseFactory;
import com.pizzapos.support.IngredientTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(IngredientController.class)
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateIngredientUseCase createIngredientUseCase;

    @MockitoBean
    private GetIngredientByIdUseCase getIngredientByIdUseCase;

    @MockitoBean
    private IngredientPresentationMapper ingredientPresentationMapper;

    @MockitoBean
    private ResponseFactory responseFactory;

    @Test
    @DisplayName("Should Create Ingredient Successfully")
    void shouldCreateIngredientSuccessfully() throws Exception {
        // Given
        CreateIngredientRequest request = new CreateIngredientRequest();

        request.setName("Mozzarella");
        request.setUnit((IngredientUnit.KILOGRAM));
        request.setStock(new BigDecimal("20"));
        request.setMinimumStock(new BigDecimal("5"));
        request.setCost(new BigDecimal("180.00"));

        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        IngredientResponse response = new IngredientResponse();

        response.setId(1L);
        response.setName("Mozzarella");
        response.setUnit(IngredientUnit.KILOGRAM);
        response.setStock(new BigDecimal("20"));
        response.setMinimumStock(new BigDecimal("5"));
        response.setCost(new BigDecimal("180.00"));
        response.setStatus(IngredientStatus.ACTIVE);

        when(ingredientPresentationMapper.toDomain(any(CreateIngredientRequest.class)))
                .thenReturn(ingredient);
        when(createIngredientUseCase.createIngredient(ingredient))
                .thenReturn(ingredient);
        when(ingredientPresentationMapper.toResponse(ingredient))
                .thenReturn(response);

        ApiResponse<IngredientResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        Messages.INGREDIENT_CREATED,
                        "test-trace-id",
                        response,
                        null
                );

        when(responseFactory.success(
                anyString(),
                any(IngredientResponse.class)
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(
                post("/api/v1/ingredients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Messages.INGREDIENT_CREATED))
                .andExpect(jsonPath("$.traceId").value("test-trace-id"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Mozzarella"))
                .andExpect(jsonPath("$.data.cost").value(180.00));

        verify(ingredientPresentationMapper).toDomain(any(CreateIngredientRequest.class));
        verify(createIngredientUseCase).createIngredient(ingredient);
        verify(ingredientPresentationMapper).toResponse(ingredient);
        verify(responseFactory).success(anyString(), eq(response));
    }

    @Test
    @DisplayName("Should return 400 when ingredient name is missing")
    void shouldReturnBadRequestWhenIngredientNameIsMissing() throws Exception {
        // Given
        CreateIngredientRequest request = new CreateIngredientRequest();

        request.setName("");
        request.setUnit(null);
        request.setStock(new BigDecimal("0"));
        request.setMinimumStock(new BigDecimal("0"));
        request.setCost(new BigDecimal("0"));

        // When & Then
        mockMvc.perform(
                post("/api/v1/ingredients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createIngredientUseCase);
    }

    @Test
    @DisplayName("Should return 400 when ingredient already exists")
    void shouldReturnBadRequestWhenIngredientAlreadyExists() throws Exception {
        // Given
        CreateIngredientRequest request = new CreateIngredientRequest();

        request.setName("Mozzarella");
        request.setUnit(IngredientUnit.KILOGRAM);
        request.setStock(new BigDecimal("20"));
        request.setMinimumStock(new BigDecimal("5"));
        request.setCost(new BigDecimal("180.00"));

        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .build();

        when(ingredientPresentationMapper.toDomain(any(CreateIngredientRequest.class)))
                .thenReturn(ingredient);
        when(createIngredientUseCase.createIngredient(ingredient))
                .thenThrow(new BusinessException(Messages.INGREDIENT_ALREADY_EXISTS));

        // When & Then
        mockMvc.perform(
                post("/api/v1/ingredients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest());

        verify(createIngredientUseCase).createIngredient(ingredient);
    }

    @Test
    @DisplayName("Should return ingredient by id successfully")
    void shouldReturnIngredientByIdSuccessfully() throws Exception {
        // Given
        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .withId(1L)
                .build();

        IngredientResponse response = new IngredientResponse();

        response.setId(1L);
        response.setName("Mozzarella");
        response.setUnit(IngredientUnit.KILOGRAM);
        response.setStock(new BigDecimal("20"));
        response.setMinimumStock(new BigDecimal("5"));
        response.setCost(new BigDecimal("180.00"));
        response.setStatus(IngredientStatus.ACTIVE);

        when(getIngredientByIdUseCase.getIngredientById(1L))
                .thenReturn(ingredient);
        when(ingredientPresentationMapper.toResponse(ingredient))
                .thenReturn(response);

        ApiResponse<IngredientResponse> apiResponse = new ApiResponse<>(
                true,
                Messages.INGREDIENT_FOUND,
                "test-trace-id",
                response,
                null
        );

        when(responseFactory.success(
                anyString(),
                any(IngredientResponse.class)
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(
                get("/api/v1/ingredients/{id}", 1L)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Messages.INGREDIENT_FOUND))
                .andExpect(jsonPath("$.traceId").value("test-trace-id"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Mozzarella"))
                .andExpect(jsonPath("$.data.unit").value("KILOGRAM"))
                .andExpect(jsonPath("$.data.stock").value(20))
                .andExpect(jsonPath("$.data.minimumStock").value(5))
                .andExpect(jsonPath("$.data.cost").value(180.00))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        verify(getIngredientByIdUseCase).getIngredientById(1L);
        verify(ingredientPresentationMapper).toResponse(ingredient);
        verify(responseFactory).success(anyString(), eq(response));
    }

    @Test
    @DisplayName("Should return 404 when ingredient does not exist")
    void shouldReturnNotFoundWhenIngredientDoesNotExist() throws Exception {
        // Given
        Long ingredientId = 999L;

        ResourceNotFoundException exception =
                new ResourceNotFoundException(Messages.INGREDIENT_NOT_FOUND);

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        doThrow(exception)
                .when(getIngredientByIdUseCase)
                .getIngredientById(ingredientId);

        when(responseFactory.error(HttpStatus.NOT_FOUND, Messages.INGREDIENT_NOT_FOUND))
                .thenReturn(errorResponse);

        // When & Then
        mockMvc.perform(
                        get("/api/v1/ingredients/{id}", ingredientId)
                )
                .andExpect(status().isNotFound());

        verify(getIngredientByIdUseCase)
                .getIngredientById(ingredientId);
        verify(responseFactory).error(HttpStatus.NOT_FOUND, Messages.INGREDIENT_NOT_FOUND);
    }

}
