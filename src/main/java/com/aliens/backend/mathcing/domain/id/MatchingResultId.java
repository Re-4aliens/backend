package com.aliens.backend.mathcing.domain.id;

import com.aliens.backend.mathcing.domain.MatchingRound;
import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class MatchingResultId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_round")
    private MatchingRound matchingRound;

    @Column(name = "matching_member_id")
    private Long matchingMemberId;

    @Column(name = "matched_member_id")
    private Long matchedMemberId;

    protected MatchingResultId() {
    }

    private MatchingResultId(final MatchingRound matchingRound, final Long matchingMemberId, final Long matchedMemberId) {
        this.matchingRound = matchingRound;
        this.matchingMemberId = matchingMemberId;
        this.matchedMemberId = matchedMemberId;
    }

    public static MatchingResultId of(MatchingRound matchingRound, Long matchingMemberId, Long matchedMemberId) {
        return new MatchingResultId(matchingRound, matchingMemberId, matchedMemberId);
    }

    public MatchingRound getMatchingRound() {
        return matchingRound;
    }

    public Long getMatchingMemberId() {
        return matchingMemberId;
    }

    public Long getMatchedMemberId() {
        return matchedMemberId;
    }
}
