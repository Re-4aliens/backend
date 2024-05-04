package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum BoardSuccess implements SuccessCode {
    POST_BOARD_SUCCESS(HttpStatus.OK, "B001", "게시글 등록에 성공했습니다."),
    GET_ALL_BOARDS_SUCCESS(HttpStatus.OK, "B002", "전체 게시글 조회에 성공했습니다."),
    GET_ALL_BOARDS_WITH_CATEGORY_SUCCESS(HttpStatus.OK, "B003", "카테고리에 따른 게시글 조회에 성공했습니다."),
    SEARCH_ALL_BOARDS_SUCCESS(HttpStatus.OK, "B004", "전체 게시글 검색에 성공했습니다."),
    GET_ALL_ANNOUNCEMENT_BOARDS_SUCCESS(HttpStatus.OK, "B005", "공지사항 게시글 조회에 성공했습니다."),
    GET_MY_BOARD_PAGE_SUCCESS(HttpStatus.OK, "B006", "본인이 작성한 게시글 조회에 성공했습니다."),
    SEARCH_BOARD_WITH_CATEGORY_SUCCESS(HttpStatus.OK, "B007", "특정 카테고리의 게시글 검색에 성공했습니다."),
    DELETE_BOARD_SUCCESS(HttpStatus.OK, "B008", "게시글 삭제에 성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    BoardSuccess(final HttpStatus httpStatus, final String code, final String message) {
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
