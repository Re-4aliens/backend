package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum MatchingSuccess implements SuccessCode {
    APPLY_MATCHING_SUCCESS(HttpStatus.OK, "MA001", "매칭 신청 성공"),
    GET_MATCHING_APPLICATION_STATUS_SUCCESS(HttpStatus.OK, "MA002", "매칭 신청 정보 조회 성공"),
    CANCEL_MATCHING_APPLICATION_SUCCESS(HttpStatus.OK, "MA003", "매칭 신청 취소 성공"),
    GET_MATCHING_PARTNERS_SUCCESS(HttpStatus.OK, "MA004", "매칭 파트너 조회 성공"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    MatchingSuccess(final HttpStatus httpStatus, final String code, final String message) {
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
