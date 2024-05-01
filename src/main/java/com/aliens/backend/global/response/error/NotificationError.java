package com.aliens.backend.global.response.error;

import org.springframework.http.HttpStatus;

public enum NotificationError implements ErrorCode {

    IS_NOT_OWNER(HttpStatus.BAD_REQUEST, "N1", "해당 알림의 주인이 아닙니다.");

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    NotificationError(final HttpStatus httpStatusCode, final String code, final String message) {
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