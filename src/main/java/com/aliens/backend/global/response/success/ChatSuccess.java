package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum ChatSuccess implements SuccessCode {
    SEND_MESSAGE_SUCCESS(HttpStatus.OK, "CH001", "메시지 전송 성공"),
    READ_MESSAGES_SUCCESS(HttpStatus.OK, "CH002", "메시지 읽음 처리 성공"),
    GET_SUMMARIES_SUCCESS(HttpStatus.OK, "CH003", "채팅요약 조회 성공"),
    GET_MESSAGES_SUCCESS(HttpStatus.OK, "CH004", "채팅 메시지들 조회 성공"),
    REPORT_SUCCESS(HttpStatus.OK, "CH005", "신고 성공"),
    BLOCK_SUCCESS(HttpStatus.OK, "CH006", "차단 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ChatSuccess(final HttpStatus httpStatus, final String code, final String message) {
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