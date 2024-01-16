package com.aliens.backend.global.exception;

import com.aliens.backend.global.error.ErrorCode;

public class BaseException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public BaseException(final ErrorCode code) {
        this.errorResponse = ErrorResponse.from(code);
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public String getMessage() {
        return errorResponse.getMessage();
    }
}