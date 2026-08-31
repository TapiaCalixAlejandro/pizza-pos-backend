package com.pizzapos.catalog.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzapos.catalog.domain.model.Pizza;
import com.pizzapos.catalog.domain.ports.in.pizza.CreatePizzaUseCase;
import com.pizzapos.catalog.presentation.dto.pizza.request.CreatePizzaRequest;
import com.pizzapos.catalog.presentation.dto.pizza.response.PizzaResponse;
import com.pizzapos.catalog.presentation.mapper.PizzaPresentationMapper;
import com.pizzapos.shared.response.ResponseFactory;
import com.pizzapos.support.PizzaTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PizzaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ResponseFactory responseFactory;

    @Mock
    private CreatePizzaUseCase createPizzaUseCase;

    @Mock
    private PizzaPresentationMapper pizzaPresentationMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        PizzaController controller =
                new PizzaController(
                        responseFactory,
                        createPizzaUseCase,
                        pizzaPresentationMapper
                );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreatePizzaSuccessfully() throws Exception {

        CreatePizzaRequest request =
                PizzaTestDataBuilder
                        .aPizza()
                        .buildRequest();

        Pizza pizza =
                PizzaTestDataBuilder
                        .aPizza()
                        .build();

        Pizza savedPizza =
                PizzaTestDataBuilder
                        .aPizza()
                        .withId(1L)
                        .build();

        PizzaResponse response =
                new PizzaResponse();

        when(pizzaPresentationMapper.toDomain(any(
                CreatePizzaRequest.class
        ))).thenReturn(pizza);

        when(createPizzaUseCase.createPizza(pizza))
                .thenReturn(savedPizza);

        when(pizzaPresentationMapper.toResponse(savedPizza))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/pizzas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated());

        verify(pizzaPresentationMapper)
                .toDomain(any(CreatePizzaRequest.class));

        verify(createPizzaUseCase)
                .createPizza(pizza);

        verify(pizzaPresentationMapper)
                .toResponse(savedPizza);
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid()
            throws Exception {

        CreatePizzaRequest request =
                PizzaTestDataBuilder
                        .aPizza()
                        .withoutIngredients()
                        .buildRequest();

        mockMvc.perform(
                        post("/api/v1/pizzas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(createPizzaUseCase);
    }
}