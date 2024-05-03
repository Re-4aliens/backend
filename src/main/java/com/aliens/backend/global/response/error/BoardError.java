package com.aliens.backend.global.response.error;

import org.springframework.http.HttpStatus;

public enum BoardError implements ErrorCode {

    INVALID_BOARD_ID(HttpStatus.BAD_REQUEST, "B1", "올바르지 않은 게시판 아이디입니다."),
    NOT_WRITER(HttpStatus.BAD_REQUEST,"B2" ,"게시글 생성자가 아닙니다." ),
    INVALID_COMMENT_ID(HttpStatus.BAD_REQUEST,"B3" ,"올바르지 않은 댓글 아이디입니다." ),
    INVALID_COMMENT_WRITER(HttpStatus.BAD_REQUEST,"B4" ,"댓글 저자가 아닙니다." );

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    BoardError(final HttpStatus httpStatusCode, final String code, final String message) {
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