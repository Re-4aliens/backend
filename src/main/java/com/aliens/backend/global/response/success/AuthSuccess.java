package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum AuthSuccess implements SuccessCode {
    GENERATE_TOKEN_SUCCESS(HttpStatus.OK, "A001", "인증 토큰 발행에 성공했습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "A002", "로그아웃에 성공했습니다."),
    REISSUE_TOKEN_SUCCESS(HttpStatus.OK, "A003", "토큰 재발급에 성공했습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    AuthSuccess(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
