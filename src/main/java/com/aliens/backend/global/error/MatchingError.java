package com.aliens.backend.global.error;

import org.springframework.http.HttpStatus;

public enum MatchingError implements ErrorCode {
    NOT_FOUND_MATCHING_ROUND(HttpStatus.NOT_FOUND, "MA1", "매칭 회차를 찾을 수 없음"),
    NOT_VALID_MATCHING_TIME(HttpStatus.BAD_REQUEST, "MA2", "매칭 시간이 아님"),

    ;

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    MatchingError(final HttpStatus httpStatusCode, final String developCode, final String message) {
        this.httpStatusCode = httpStatusCode;
        this.developCode = developCode;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatusCode;
    }

    @Override
    public String getDevelopCode() {
        return developCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
