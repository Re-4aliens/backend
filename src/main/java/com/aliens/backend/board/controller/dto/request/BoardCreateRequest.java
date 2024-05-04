package com.aliens.backend.board.controller.dto.request;


import com.aliens.backend.board.domain.enums.BoardCategory;

public record BoardCreateRequest(
        String title,
        String content,
        BoardCategory boardCategory) {
    public static BoardCreateRequest from(final MarketBoardCreateRequest marketRequest) {
        return new BoardCreateRequest(marketRequest.title(),
                marketRequest.content(),
                BoardCategory.MARKET);
    }
}
