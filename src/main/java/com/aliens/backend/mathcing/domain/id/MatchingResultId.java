package com.aliens.backend.mathcing.domain.id;

import com.aliens.backend.mathcing.domain.MatchingRound;
import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class MatchingResultId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_round")
    private MatchingRound matchingRound;

    @Column(name = "member_id")
    private Long memberId;

    protected MatchingResultId() {
    }

    private MatchingResultId(final MatchingRound matchingRound, final Long memberId) {
        this.matchingRound = matchingRound;
        this.memberId = memberId;
    }

    public MatchingResultId of(MatchingRound matchingRound, Long memberId) {
        return new MatchingResultId(matchingRound, memberId);
    }

    public MatchingRound getMatchingRound() {
        return matchingRound;
    }

    public Long getMemberId() {
        return memberId;
    }
}
