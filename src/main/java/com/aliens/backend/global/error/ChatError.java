package com.aliens.backend.global.error;

import org.springframework.http.HttpStatus;

public enum ChatError implements ErrorCode {

    INVALID_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "CH1", "메시지 타입이 잘못되었습니다."),
    INVALID_ROOM_ACCESS(HttpStatus.FORBIDDEN, "CH2", "채팅방 접근 권한이 없습니다.")
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