package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum GreatSuccess implements SuccessCode {
    GREAT_AT_BOARD_SUCCESS(HttpStatus.OK, "G001", "게시글 좋아요 등록에 성공했습니다."),
    GET_ALL_GREAT_BOARDS_SUCCESS(HttpStatus.OK, "G002", "좋아요한 게시글 전체 조회에 성공했습니다."),
    DELETE_AT_BOARD_SUCCESS(HttpStatus.OK, "G003", "게시글 좋아요 취소에 성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    GreatSuccess(final HttpStatus httpStatus, final String code, final String message) {
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
