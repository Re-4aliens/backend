package com.aliens.backend.global.response.error;

import org.springframework.http.HttpStatus;

public enum EmailError implements ErrorCode {

    NULL_EMAIL(HttpStatus.UNPROCESSABLE_ENTITY, "E1", "해당 EmailAuthentication 엔티티 조회 불가"),
    NOT_AUTHENTICATED_EMAIL(HttpStatus.UNPROCESSABLE_ENTITY, "E2", "인증되지 않은 이메일"),
    ALREADY_MEMBER(HttpStatus.BAD_REQUEST, "E3", "해당 이메일로 이미 회원가입이 되어있습니다.");

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    EmailError(final HttpStatus httpStatusCode, final String code, final String message) {
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