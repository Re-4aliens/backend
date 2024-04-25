package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum ReportSuccess implements SuccessCode {
    REPORT_BOARD_SUCCESS(HttpStatus.OK, "R001", "게시글 신고가 완료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ReportSuccess(final HttpStatus httpStatus, final String code, final String message) {
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
