package com.aliens.backend.global.response.log;

import com.aliens.backend.global.response.error.ErrorCode;
import com.aliens.backend.global.response.success.SuccessCode;

import java.util.Arrays;
import java.util.UUID;

public record InfoLogResponse(
        String logId,
        String status,
        String message,
        Object data
) {
    private static final int MAX_STACK_TRACE_ELEMENTS = 5;

    public static InfoLogResponse from(SuccessCode code, Object data) {
        return new InfoLogResponse(
                UUID.randomUUID().toString().substring(0, 8),
                code.getHttpStatus().toString(),
                code.getMessage(),
                data);
    }

    public static InfoLogResponse from(ErrorCode code, Object data) {
        return new InfoLogResponse(
                UUID.randomUUID().toString().substring(0, 8),
                code.getHttpStatus().toString(),
                code.getMessage(),
                data);

    }
    public static InfoLogResponse from(ErrorCode code) {
        return new InfoLogResponse(
                UUID.randomUUID().toString().substring(0, 8),
                code.getHttpStatus().toString(),
                code.getMessage(),
                code.getDevelopCode());
    }

    public static InfoLogResponse from(Throwable exception, Object data) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        String limitedStackTrace = Arrays.stream(stackTrace)
                .limit(MAX_STACK_TRACE_ELEMENTS)
                .map(StackTraceElement::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("No stack trace available");

        return new InfoLogResponse(
                UUID.randomUUID().toString().substring(0, 8),
                exception.getMessage(),
                limitedStackTrace,
                data
        );
    }
}
