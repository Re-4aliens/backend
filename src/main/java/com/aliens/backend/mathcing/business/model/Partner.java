package com.aliens.backend.mathcing.business.model;

public record Partner(
        Relationship relationship,
        Long memberId
) {
    public static Partner of(Relationship relationship, Long memberId) {
        return new Partner(relationship, memberId);
    }
}
