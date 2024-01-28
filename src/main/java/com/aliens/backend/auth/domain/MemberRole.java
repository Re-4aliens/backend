package com.aliens.backend.auth.domain;

public enum MemberRole {
    ADMIN(0,"관리자"),
    MEMBER(1,"회원");

    private final Integer code;
    private final String description;

    MemberRole(final Integer code, final String description) {
        this.code = code;
        this.description = description;
    }

    public static MemberRole of(final Integer code) {
        for (MemberRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }
}
