package com.pizzapos.shared.tracing;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultTraceProviderTest {

    private final DefaultTraceProvider traceProvider = new DefaultTraceProvider();

    @Test
    void shouldGenerateTraceId() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String traceId = traceProvider.getTraceId();

        assertNotNull(traceId);
        assertDoesNotThrow(() -> UUID.fromString(traceId));

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldReturnSameTraceIdWithinSameRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String firstTraceId = traceProvider.getTraceId();
        String secondTraceId = traceProvider.getTraceId();

        assertNotNull(firstTraceId);
        assertEquals(firstTraceId, secondTraceId);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldGenerateDifferentTraceIdForDifferentRequests() {
        MockHttpServletRequest firstRequest = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(firstRequest));

        String firstTraceId = traceProvider.getTraceId();

        RequestContextHolder.resetRequestAttributes();

        MockHttpServletRequest secondRequest = new MockHttpServletRequest();

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(secondRequest));

        String secondTraceId = traceProvider.getTraceId();

        assertNotNull(firstTraceId);
        assertNotNull(secondTraceId);
        assertNotEquals(firstTraceId, secondTraceId);

        RequestContextHolder.resetRequestAttributes();
    }

}
