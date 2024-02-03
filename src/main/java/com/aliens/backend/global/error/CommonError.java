package com.aliens.backend.global.error;

import org.springframework.http.HttpStatus;

public enum CommonError implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.NOT_FOUND, "S1", "에러"),
    ENCODE_ERROR(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED,"S2" ,"암-복호화 실패" );

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    CommonError(final HttpStatus httpStatusCode, final String code, final String message) {
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