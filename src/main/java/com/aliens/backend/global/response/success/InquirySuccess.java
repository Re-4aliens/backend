package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum InquirySuccess implements SuccessCode {
    CREATE_INQUIRY_SUCCESS(HttpStatus.OK, "I001", "문의 등록에 성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    InquirySuccess(final HttpStatus httpStatus, final String code, final String message) {
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