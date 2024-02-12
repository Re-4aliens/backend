package com.aliens.backend.global.response.error;

import org.springframework.http.HttpStatus;

public enum TokenError implements ErrorCode {

    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"T1", "올바르지 않은 AccessToken"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"T2", "만료된 AccessToken"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "T3", "만료된 ReFreshToken"),
    NULL_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"T4", "존재하지 않은 ReFreshToken 접근"),
    NOT_ACCESS_TOKEN_FOR_REISSUE(HttpStatus.BAD_REQUEST,"T5","재발급하기에는 유효기간이 남은 AccessToken");

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    TokenError(final HttpStatus httpStatusCode, final String code, final String message) {
        this.httpStatusCode = httpStatusCode;
        this.developCode = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatusCode;
    }

    @Override
    public String getDevelopCode() {
        return developCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
