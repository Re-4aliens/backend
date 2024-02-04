package com.aliens.backend.global.success;

import org.springframework.http.HttpStatus;

public enum ChatSuccessCode implements SuccessCode {
    SEND_MESSAGE_SUCCESS(HttpStatus.CREATED, "CH001", "메시지 전송 성공"),
    READ_MESSAGES_SUCCESS(HttpStatus.OK, "CH002", "메시지 읽음 처리 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ChatSuccessCode(final HttpStatus httpStatus, final String code, final String message) {
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