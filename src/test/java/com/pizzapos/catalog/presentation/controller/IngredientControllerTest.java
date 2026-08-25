package com.pizzapos.catalog.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzapos.catalog.domain.enums.IngredientStatus;
import com.pizzapos.catalog.domain.enums.IngredientUnit;
import com.pizzapos.catalog.domain.model.Ingredient;
import com.pizzapos.catalog.domain.ports.in.ingredient.CreateIngredientUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.DeleteIngredientUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.GetAllIngredientsUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.GetIngredientByIdUseCase;
import com.pizzapos.catalog.domain.ports.in.ingredient.UpdateIngredientUseCase;
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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

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
    private DeleteIngredientUseCase deleteIngredientUseCase;

    @MockitoBean
    private UpdateIngredientUseCase updateIngredientUseCase;

    @MockitoBean
    private GetAllIngredientsUseCase getAllIngredientsUseCase;

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

    @Test
    @DisplayName("Should return all products successfully")
    void shouldReturnAllProductsSuccessfully() throws Exception {
        // Given
        Ingredient mozzarella = new Ingredient();
        mozzarella.setId(1L);
        mozzarella.setName("Mozzarella");
        mozzarella.setUnit(IngredientUnit.KILOGRAM);
        mozzarella.setStock(new BigDecimal("20"));
        mozzarella.setMinimumStock(new BigDecimal("5"));
        mozzarella.setCost(new BigDecimal("180.00"));
        mozzarella.setStatus(IngredientStatus.ACTIVE);

        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setId(1L);
        ingredientResponse.setName("Mozzarella");
        ingredientResponse.setUnit(IngredientUnit.KILOGRAM);
        ingredientResponse.setStock(new BigDecimal("20"));
        ingredientResponse.setMinimumStock(new BigDecimal("5"));
        ingredientResponse.setCost(new BigDecimal("180.00"));
        ingredientResponse.setStatus(IngredientStatus.ACTIVE);

        when(getAllIngredientsUseCase.getAllIngredients()).thenReturn(List.of(mozzarella));
        when(ingredientPresentationMapper.toResponseList(List.of(mozzarella))).thenReturn(List.of(ingredientResponse));

        ApiResponse<List<IngredientResponse>> apiResponse = new ApiResponse<>(
                true,
                "Ingredients retrieved successfully",
                "test-trace-id",
                List.of(ingredientResponse),
                null
        );

        when(responseFactory.<List<IngredientResponse>>success(
                anyString(),
                anyList()
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(
                get("/api/v1/ingredients")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.traceId").value("test-trace-id"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Mozzarella"))
                .andExpect(jsonPath("$.data[0].unit").value("KILOGRAM"))
                .andExpect(jsonPath("$.data[0].stock").value(20))
                .andExpect(jsonPath("$.data[0].minimumStock").value(5))
                .andExpect(jsonPath("$.data[0].cost").value(180.00))
                .andExpect(jsonPath("$.data[0].status").value("ACTIVE"));

        verify(getAllIngredientsUseCase, times(1))
                .getAllIngredients();
        verify(ingredientPresentationMapper, times(1))
                .toResponseList(List.of(mozzarella));
        verify(responseFactory, times(1))
                .success(anyString(), eq(List.of(ingredientResponse)));
    }

    @Test
    @DisplayName("Should return empty list when there are no ingredients")
    void shouldReturnEmptyListWhenThereAreNoIngredients() throws Exception {
        // Given
        when(getAllIngredientsUseCase.getAllIngredients())
                .thenReturn(List.of());
        when(ingredientPresentationMapper.toResponseList(List.of()))
                .thenReturn(List.of());

        ApiResponse<List<IngredientResponse>> apiResponse = new ApiResponse<>(
                true,
                "Ingredients retrieved successfully",
                "test-trace-id",
                List.of(),
                null
        );

        when(responseFactory.<List<IngredientResponse>>success(
                anyString(),
                anyList()
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(
                get("/api/v1/ingredients")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(getAllIngredientsUseCase, times(1))
                .getAllIngredients();
        verify(ingredientPresentationMapper, times(1))
                .toResponseList(List.of());
    }

    @Test
    @DisplayName("Should soft delete ingredient successfully")
    void shouldSoftDeleteIngredientSuccessfully() throws Exception {
        // Given
        Long ingredientId = 1L;

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                true,
                "Ingredient deleted successfully",
                "test-trace-id",
                null,
                null
        );

        doNothing()
                .when(deleteIngredientUseCase)
                .deletedIngredientById(ingredientId);

        when(responseFactory.<Void>success(
                anyString(),
                isNull()
        )).thenReturn(apiResponse);

        // When & Then
        mockMvc.perform(
                delete("/api/v1/ingredients/{id}", ingredientId)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ingredient deleted successfully"))
                .andExpect(jsonPath("$.traceId").value("test-trace-id"));

        verify(deleteIngredientUseCase).deletedIngredientById(ingredientId);
        verify(responseFactory).success(anyString(), isNull());
    }

    @Test
    @DisplayName("Should return 404 when ingredient does not exist")
    void shouldReturnNotFoundIngredientDoesNotExist() throws Exception {
        // Given
        Long ingredientId = 1L;

        ResourceNotFoundException exception =
                new ResourceNotFoundException(Messages.INGREDIENT_NOT_FOUND);

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        doThrow(exception)
                .when(deleteIngredientUseCase)
                .deletedIngredientById(ingredientId);

        when(responseFactory.error(
                HttpStatus.NOT_FOUND,
                Messages.INGREDIENT_NOT_FOUND
        )).thenReturn(errorResponse);

        // When & Then
        mockMvc.perform(
                delete("/api/v1/ingredients/{id}", ingredientId)
        )
                .andExpect(status().isNotFound());

        verify(deleteIngredientUseCase).deletedIngredientById(ingredientId);
        verify(responseFactory)
                .error(
                        HttpStatus.NOT_FOUND,
                        Messages.INGREDIENT_NOT_FOUND
                );
    }

    @Test
    @DisplayName("Should update ingredient successfully")
    void shouldUpdateIngredientSuccessfully() throws Exception {
        // Given
        Long ingredientId = 1L;

        CreateIngredientRequest request = new CreateIngredientRequest();

        request.setName("Mass");
        request.setUnit(IngredientUnit.KILOGRAM);
        request.setStock(new BigDecimal("20"));
        request.setMinimumStock(new BigDecimal("5"));
        request.setCost(new BigDecimal("180.00"));

        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .withName("Mass")
                .build();

        ingredient.setId(ingredientId);
        ingredient.setStock(new BigDecimal("20"));
        ingredient.setMinimumStock(new BigDecimal("5"));
        ingredient.setCost(new BigDecimal("180.00"));

        IngredientResponse response = new IngredientResponse();

        response.setId(ingredientId);
        response.setName("Mass");
        response.setStock(new BigDecimal("20"));
        response.setMinimumStock(new BigDecimal("5"));
        response.setCost(new BigDecimal("180.00"));

        when(ingredientPresentationMapper.toDomain(any(CreateIngredientRequest.class)))
                .thenReturn(ingredient);
        when(updateIngredientUseCase.updateIngredient(ingredientId, ingredient))
                .thenReturn(ingredient);
        when(ingredientPresentationMapper.toResponse(ingredient))
                .thenReturn(response);

        ApiResponse<IngredientResponse> apiResponse = new ApiResponse<>(
                true,
                Messages.INGREDIENT_UPDATED,
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
                put("/api/v1/ingredients/{id}", ingredientId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(Messages.INGREDIENT_UPDATED))
                .andExpect(jsonPath("$.traceId").value("test-trace-id"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Mass"))
                .andExpect(jsonPath("$.data.stock").value(20))
                .andExpect(jsonPath("$.data.minimumStock").value(5))
                .andExpect(jsonPath("$.data.cost").value(180.00));

        verify(ingredientPresentationMapper).toDomain(any(CreateIngredientRequest.class));
        verify(updateIngredientUseCase).updateIngredient(ingredientId, ingredient);
        verify(ingredientPresentationMapper).toResponse(ingredient);
        verify(responseFactory).success(anyString(), eq(response));
    }

    @Test
    @DisplayName("Should return 400 when ingredient update does not exist")
    void shouldReturnNotFoundWhenIngredientUpdateDoesNotExist() throws Exception {
        // Given
        Long ingredientId = 999L;

        CreateIngredientRequest request = new CreateIngredientRequest();

        request.setName("Mass");
        request.setUnit(IngredientUnit.KILOGRAM);
        request.setStock(new BigDecimal("20"));
        request.setMinimumStock(new BigDecimal("5"));
        request.setCost(new BigDecimal("180.00"));

        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .withName("Mass")
                .build();

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        when(ingredientPresentationMapper.toDomain(any(CreateIngredientRequest.class)))
                .thenReturn(ingredient);
        when(updateIngredientUseCase.updateIngredient(ingredientId, ingredient))
                .thenThrow(new ResourceNotFoundException(Messages.INGREDIENT_NOT_FOUND));
        when(responseFactory.error(
                HttpStatus.NOT_FOUND,
                Messages.INGREDIENT_NOT_FOUND
        )).thenReturn(errorResponse);

        // When & Then
        mockMvc.perform(
                put("/api/v1/ingredients/{id}", ingredientId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isNotFound());

        verify(ingredientPresentationMapper).toDomain(any(CreateIngredientRequest.class));
        verify(updateIngredientUseCase).updateIngredient(ingredientId, ingredient);
        verify(responseFactory).error(HttpStatus.NOT_FOUND, Messages.INGREDIENT_NOT_FOUND);
    }

    @Test
    @DisplayName("Should return bad request when ingredient name already exists")
    void shouldReturnBadRequestWhenIngredientNameAlreadyExists() throws Exception {
        // Given
        Long ingredientId = 1L;

        CreateIngredientRequest request = new CreateIngredientRequest();

        request.setName("Mass");
        request.setUnit(IngredientUnit.KILOGRAM);
        request.setStock(new BigDecimal("20"));
        request.setMinimumStock(new BigDecimal("5"));
        request.setCost(new BigDecimal("180.00"));

        Ingredient ingredient = IngredientTestDataBuilder
                .anIngredient()
                .withName("Mass")
                .build();

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        when(ingredientPresentationMapper.toDomain(any(CreateIngredientRequest.class)))
                .thenReturn(ingredient);
        when(updateIngredientUseCase.updateIngredient(ingredientId, ingredient))
                .thenThrow(new BusinessException(Messages.INGREDIENT_ALREADY_EXISTS));
        when(responseFactory.error(
                HttpStatus.BAD_REQUEST,
                Messages.INGREDIENT_ALREADY_EXISTS
        )).thenReturn(errorResponse);

        // When & Then
        mockMvc.perform(
                put("/api/v1/ingredients/{id}", ingredientId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest());

        verify(ingredientPresentationMapper).toDomain(any(CreateIngredientRequest.class));
        verify(updateIngredientUseCase).updateIngredient(ingredientId, ingredient);
        verify(responseFactory).error(HttpStatus.BAD_REQUEST, Messages.INGREDIENT_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("Should return 400 when update request is invalid")
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid() throws Exception {
        // Given
        CreateIngredientRequest request = new CreateIngredientRequest();

        request.setName("");
        request.setUnit(null);
        request.setStock(BigDecimal.ZERO);
        request.setMinimumStock(BigDecimal.ZERO);
        request.setCost(BigDecimal.ZERO);

        // When & Then
        mockMvc.perform(
                put("/api/v1/ingredients/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(updateIngredientUseCase);
    }

}
