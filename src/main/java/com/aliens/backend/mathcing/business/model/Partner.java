package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.auth.domain.Member;

public record Partner(
        Relationship relationship,
        Member member
) {
    public static Partner of(final Relationship relationship, final Member member) {
        return new Partner(relationship, member);
    }
}
