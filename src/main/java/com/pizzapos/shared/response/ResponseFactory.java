package com.pizzapos.shared.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseFactory {

    private ResponseFactory() {
    }

    private static String getTraceId() {
        // Por ahora no tenemos trazabilidad
        return null;
    }

    public static <T> ApiResponse<T> success(
            String message,
            T data
    ) {

        return new ApiResponse<>(
                true,
                message,
                getTraceId(),
                data,
                LocalDateTime.now()
        );
    }

    public static ApiErrorResponse error(
            HttpStatus status,
            String message,
            List<String> details
    ) {

        return new ApiErrorResponse(
                false,
                status.value(),
                status.getReasonPhrase(),
                message,
                details,
                getTraceId(),
                LocalDateTime.now()
        );
    }

    public static ApiErrorResponse error(
            HttpStatus status,
            String message
    ) {

        return  error(status, message, List.of());
    }

}
