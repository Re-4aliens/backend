package com.aliens.backend.global.exception;

import com.aliens.backend.global.error.ErrorCode;

public class RestApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public RestApiException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
