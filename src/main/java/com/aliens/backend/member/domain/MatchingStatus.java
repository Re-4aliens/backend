package com.aliens.backend.member.domain;

public enum MatchingStatus {

    APPLIED_MATCHED("AppliedAndMatched"),
    NOT_APPLIED_MATCHED("NotAppliedAndMatched"),
    APPLIED_NOT_MATCHED("AppliedAndNotMatched"),
    NOT_APPLIED_NOT_MATCHED("NotAppliedAndNotMatched");

    private final String message;

    MatchingStatus(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}