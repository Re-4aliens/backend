package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum EmailSuccess implements SuccessCode {
    DUPLICATE_CHECK_SUCCESS(HttpStatus.OK, "E001", "이메일 중복검사가 성공했습니다."),
    SEND_EMAIL_SUCCESS(HttpStatus.OK, "E002", "인증 이메일이 전송되었습니다."),
    EMAIL_AUTHENTICATE_SUCCESS(HttpStatus.OK, "E003", "이메일 인증에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    EmailSuccess(final HttpStatus httpStatus, final String code, final String message) {
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
