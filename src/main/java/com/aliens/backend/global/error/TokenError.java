package com.aliens.backend.global.error;

public enum TokenError implements ErrorCode {

    INVALID_TOKEN("T1", "올바르지 않은 AccessToken"),
    EXPIRED_ACCESS_TOKEN("T2", "만료된 AccessToken"),
    EXPIRED_REFRESH_TOKEN("T3", "만료된 ReFreshToken"),
    NULL_REFRESH_TOKEN("T4", "존재하지 않은 ReFreshToken 접근"),
    NOT_ACCESS_TOKEN_FOR_REISSUE("T5","재발급하기에는 유효기간이 남은 AccessToken");

    private final String code;
    private final String message;

    TokenError(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
