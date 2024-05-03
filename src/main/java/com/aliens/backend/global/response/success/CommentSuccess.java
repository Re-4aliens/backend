package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum CommentSuccess implements SuccessCode {
    PARENT_COMMENT_CREATE_SUCCESS(HttpStatus.OK, "C001", "부모 댓글 작성에 성공했습니다."),
    CHILD_COMMENT_CREATE_SUCCESS(HttpStatus.OK, "C002", "자식 댓글 작성에 성공했습니다."),
    GET_MY_COMMENTED_BOARD_PAGE_SUCCESS(HttpStatus.OK, "C003", "본인이 댓글단 게시글 조회에 성공했습니다."),
    GET_COMMENTS_BY_BOARD_ID_SUCCESS(HttpStatus.OK, "C004", "해당 게시글의 모든 댓글 조회에 성공했습니다"),
    DELETE_COMMENT_SUCCESS(HttpStatus.OK, "C005", "댓글 삭제에 성공했습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CommentSuccess(final HttpStatus httpStatus, final String code, final String message) {
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
