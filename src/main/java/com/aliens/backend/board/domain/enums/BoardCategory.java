package com.aliens.backend.board.domain.enums;

public enum BoardCategory {

    ALL("all", "전체게시판"),
    MARKET("market", "장터게시판"),
    FREE("free", "자유게시판"),
    INFO("info", "정보게시판"),
    MUSIC("music", "음악게시판"),
    GAME("game", "게임게시판"),
    FOOD("food", "음식게시판"),
    FASHION("fashion", "패션게시판"),
    ANNOUNCEMENT("announcement", "공지사항");

    private String value;
    private String name;

    BoardCategory(final String value, final String name) {
        this.value = value;
        this.name = name;
    }

    public static BoardCategory from(final String request) {
        if (request == null) {
            return ALL;
        }

        for (BoardCategory category : BoardCategory.values()) {
            if (category.value.equals(request)) return category;
        }

        throw new IllegalArgumentException("일치하는 카테고리 없음");
    }
}