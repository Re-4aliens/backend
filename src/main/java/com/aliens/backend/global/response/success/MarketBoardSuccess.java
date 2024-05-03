package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum MarketBoardSuccess implements SuccessCode {
    CREATE_MARKET_BOARD_SUCCESS(HttpStatus.OK, "MB001", "장터 게시글 등록에 성공했습니다."),
    GET_MARKET_BOARD_PAGE_SUCCESS(HttpStatus.OK, "MB002", "장터 게시글 조회에 성공했습니다."),
    GET_MARKET_BOARD_DETAILS_SUCCESS(HttpStatus.OK, "MB003", "장터 게시글 상세 조회에 성공했습니다."),
    SEARCH_MARKET_BOARD_SUCCESS(HttpStatus.OK, "MB004", "장터 게시글 검색에 성공했습니다."),
    CHANGE_MARKET_BOARD_SUCCESS(HttpStatus.OK, "MB004", "장터 게시글 수정에 성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    MarketBoardSuccess(final HttpStatus httpStatus, final String code, final String message) {
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
