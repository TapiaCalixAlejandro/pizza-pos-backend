package com.pizzapos.shared.response;

import com.pizzapos.shared.tracing.TraceProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResponseFactoryTest {

    private final TraceProvider traceProvider = mock(TraceProvider.class);
    private final ResponseFactory responseFactory = new ResponseFactory(traceProvider);

    @Test
    @DisplayName("Should include trace ID in successful response")
    void shouldIncludeTraceIdInSuccessfulResponse() {
        // Given
        String traceId = "test-trace-id";

        when(traceProvider.getTraceId()).thenReturn(traceId);

        // When
        ApiResponse<String> response = responseFactory.success("Operation successful", "data");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Operation successful", response.getMessage());
        assertEquals(traceId, response.getTraceId());
        assertEquals("data", response.getData());
        assertNotNull(response.getTimestamp());

        verify(traceProvider, times(1)).getTraceId();
    }

    @Test
    @DisplayName("Should include trace ID in error response")
    void shouldIncludeTraceIdInErrorResponse() {
        // Given
        String traceId = "test-error-trace-id";

        when(traceProvider.getTraceId()).thenReturn(traceId);

        // When
        ApiErrorResponse response =
                responseFactory.error(
                        HttpStatus.NOT_FOUND,
                        "Product not found",
                        List.of("Product does not exist")
                );

        // Then
        assertEquals(traceId, response.getTraceId());

        verify(traceProvider, times(1)).getTraceId();
    }

}
