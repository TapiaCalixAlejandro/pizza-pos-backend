package com.pizzapos.shared.tracing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultTraceProviderTest {

    private final DefaultTraceProvider traceProvider = new DefaultTraceProvider();

    @Test
    @DisplayName("Should generate a trace ID")
    void shouldGenerateTraceId() {
        String traceId = traceProvider.getTraceId();

        assertNotNull(traceId);
        assertFalse(traceId.isBlank());
    }

    @Test
    @DisplayName("Should generate a valid UUID trace ID")
    void shouldGenerateValidUuidTraceId() {

        String traceId = traceProvider.getTraceId();

        assertDoesNotThrow(() ->
                UUID.fromString(traceId)
        );
    }

    @Test
    @DisplayName("Should generate different trace IDs")
    void shouldGenerateDifferentTraceIds() {

        String firstTraceId = traceProvider.getTraceId();
        String secondTraceId = traceProvider.getTraceId();

        assertNotNull(firstTraceId, secondTraceId);
    }

}
