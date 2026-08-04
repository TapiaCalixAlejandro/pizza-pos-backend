package com.pizzapos.shared.tracing;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DefaultTraceProvider implements TraceProvider {
    @Override
    public String getTraceId() {
        return UUID.randomUUID().toString();
    }
}
