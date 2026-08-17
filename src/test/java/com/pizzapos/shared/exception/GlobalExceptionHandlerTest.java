package com.pizzapos.shared.exception;

import com.pizzapos.shared.response.ApiErrorResponse;
import com.pizzapos.shared.response.ResponseFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Mock
    private ResponseFactory responseFactory;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Should handle ResourceNotFoundException and return 404")
    void shouldHandleResourceNotFoundException() {
        // Given
        String message = "Product not found";

        ResourceNotFoundException exception =
                new ResourceNotFoundException(message);

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        when(responseFactory.error(
                HttpStatus.NOT_FOUND,
                message
        )).thenReturn(errorResponse);

        // When
        ResponseEntity<ApiErrorResponse> response =
                exceptionHandler.handleResourceNotFound(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());

        verify(responseFactory).error(HttpStatus.NOT_FOUND, message);
    }

    @Test
    @DisplayName("Should handle BusinessException and return 400")
    void shouldHandleBusinessException() {
        // Given
        String message = "A product with this name already exists.";

        BusinessException exception = new BusinessException(message);

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        when(responseFactory.error(
                HttpStatus.BAD_REQUEST,
                message
        )).thenReturn(errorResponse);

        // When
        ResponseEntity<ApiErrorResponse> response =
                exceptionHandler.handleBusinessException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());

        verify(responseFactory).error(HttpStatus.BAD_REQUEST, message);
    }

    @Test
    @DisplayName("Should handle unexpected exception and return 500")
    void shouldHandleUnexpectedException() {
        // Given
        Exception exception = new Exception("Unexpected error");

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        doReturn(errorResponse)
                .when(responseFactory)
                .error(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred.",
                        List.of("Unexpected error")
                );

        ResponseEntity<ApiErrorResponse> response =
                exceptionHandler.handleException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());

        verify(responseFactory)
                .error(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred.",
                        List.of("Unexpected error")
                );
    }

    @Test
    @DisplayName("Should handle validation exception and return 400")
    void shouldHandleValidationException() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError nameError = new FieldError(
                "createProductRequest",
                "name",
                "Name is required."
        );

        FieldError priceError = new FieldError(
                "createProductRequest",
                "price",
                "Price must be greater than zero."
        );

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(nameError, priceError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);

        ApiErrorResponse errorResponse = new ApiErrorResponse();

        List<String> details = List.of(
                "Name is required.",
                "Price must be greater than zero."
        );

        when(responseFactory.error(
                HttpStatus.BAD_REQUEST,
                "Request validation failed.",
                details
        )).thenReturn(errorResponse);

        // When
        ResponseEntity<ApiErrorResponse> response =
                exceptionHandler.handleValidationException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(errorResponse, response.getBody());

        verify(responseFactory)
                .error(
                        HttpStatus.BAD_REQUEST,
                        "Request validation failed.",
                        details
                );
    }

}
