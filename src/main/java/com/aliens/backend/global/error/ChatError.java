package com.aliens.backend.global.error;

import org.springframework.http.HttpStatus;

public enum ChatError implements ErrorCode {

    INVALID_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "CH1", "Invalid message type"),
    ;

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    ChatError(final HttpStatus httpStatusCode, final String code, final String message) {
        this.httpStatusCode = httpStatusCode;
        this.developCode = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDevelopCode() {
        return developCode;
    }
}