package com.aliens.backend.global.error;

import org.springframework.http.HttpStatus;

public enum MemberError implements ErrorCode {

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "M1", "올바르지 않은 형식의 비밀번호"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "M2", "올바르지 않은 형식의 이메일"),
    NULL_MEMBER(HttpStatus.UNPROCESSABLE_ENTITY, "M3", "해당 Member 엔티티 조회 불가"),
    INCORRECT_PASSWORD(HttpStatus.SERVICE_UNAVAILABLE, "M4", "비밀번호 틀림"),
    WITHDRAW_MEMBER(HttpStatus.BAD_REQUEST,"M5" ,"탈퇴한 회원에 대한 접근" );

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    MemberError(final HttpStatus httpStatusCode, final String code, final String message) {
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