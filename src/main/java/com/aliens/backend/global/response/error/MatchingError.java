package com.aliens.backend.global.response.error;

import org.springframework.http.HttpStatus;

public enum MatchingError implements ErrorCode {
    NOT_FOUND_MATCHING_ROUND(HttpStatus.NOT_FOUND, "MA1", "매칭 회차를 찾을 수 없음"),
    NOT_VALID_MATCHING_RECEPTION_TIME(HttpStatus.BAD_REQUEST, "MA2", "매칭 접수 시간이 아님"),
    NOT_FOUND_MATCHING_APPLICATION_INFO(HttpStatus.NOT_FOUND, "MA3", "매칭 신청 정보 찾을 수 없음"),
    NOT_FOUND_PREFER_LANGUAGE(HttpStatus.NOT_FOUND, "MA4", "선호 언어를 찾을 수 없음"),
    INVALID_LANGUAGE_INPUT(HttpStatus.BAD_REQUEST, "MA5", "두 선호 언어가 같을 수 없음"),
    DUPLICATE_MATCHING_APPLICATION(HttpStatus.BAD_REQUEST, "MA6", "중복된 매칭 신청"),

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
