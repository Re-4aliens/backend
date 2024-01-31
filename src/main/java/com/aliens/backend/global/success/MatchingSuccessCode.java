package com.aliens.backend.global.success;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum MatchingSuccessCode implements SuccessCode {
    APPLY_MATCHING_SUCCESS(CREATED, "MA001", "매칭 신청 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    MatchingSuccessCode(final HttpStatus httpStatus, final String code, final String message) {
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
