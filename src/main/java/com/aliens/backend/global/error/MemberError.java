package com.aliens.backend.global.error;

public enum MemberError implements ErrorCode{

    INVALID_PASSWORD("M1", "올바르지 않은 형식의 비밀번호"),
    INVALID_EMAIL("M2", "올바르지 않은 형식의 이메일"),
    NULL_MEMBER("M3", "해당 Member 엔티티 조회 불가");

    private final String code;
    private final String message;

    MemberError(final String code, final String message) {
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