package com.aliens.backend.mathcing.domain;

import com.aliens.backend.mathcing.domain.id.MatchingResultId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class MatchingResult {
    @EmbeddedId
    private MatchingResultId id;

    @Column
    private Long matchingMemberId;

    @Column
    private Long matchedMemberId;

    public MatchingResultId getId() {
        return id;
    }

    public Long getMatchingMemberId() {
        return matchingMemberId;
    }

    public Long getMatchedMemberId() {
        return matchedMemberId;
    }
}
