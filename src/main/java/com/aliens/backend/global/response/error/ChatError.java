package com.aliens.backend.global.response.error;

import org.springframework.http.HttpStatus;

public enum ChatError implements ErrorCode {

    INVALID_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "CH1", "메시지 타입이 잘못되었습니다."),
    INVALID_ROOM_ACCESS(HttpStatus.FORBIDDEN, "CH2", "채팅방 접근 권한이 없습니다."),
    INVALID_REPORT_CATEGORY(HttpStatus.BAD_REQUEST, "CH3", "신고 카테고리가 잘못되었습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CH4", "채팅방을 찾을 수 없습니다."),
    ROOM_NOT_OPEN(HttpStatus.FORBIDDEN, "CH5", "채팅방이 열리지 않았습니다.")
    ;

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    ChatError(final HttpStatus httpStatusCode, final String code, final String message) {
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