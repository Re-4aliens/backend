package com.aliens.backend.mathcing.domain;

import com.aliens.backend.mathcing.domain.id.MatchingResultId;
import com.aliens.backend.mathcing.service.model.Relationship;
import jakarta.persistence.*;

@Entity
public class MatchingResult {
    @EmbeddedId
    private MatchingResultId id;

    @Enumerated(EnumType.STRING)
    private Relationship relationship;

    protected MatchingResult() {
    }

    private MatchingResult(final MatchingResultId id, final Relationship relationship) {
        this.id = id;
        this.relationship = relationship;
    }

    public static MatchingResult of(MatchingRound matchingRound,
                                    Long matchingMemberId,
                                    Long matchedMemberId,
                                    Relationship relationship) {
        return new MatchingResult(
                MatchingResultId.of(matchingRound, matchingMemberId, matchedMemberId), relationship);
    }

    public MatchingResultId getId() {
        return id;
    }

    public Relationship getRelationship() {
        return relationship;
    }
}
