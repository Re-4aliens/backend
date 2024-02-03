package com.aliens.backend.member.domain;

public enum MemberStatus {

    APPLIED_MATCHED("AppliedAndMatched"),
    NOT_APPLIED_MATCHED("NotAppliedAndMatched"),
    APPLIED_NOT_MATCHED("AppliedAndNotMatched"),
    NOT_APPLIED_NOT_MATCHED("NotAppliedAndNotMatched");

    private final String message;

    MemberStatus(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}