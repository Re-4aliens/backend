package com.aliens.backend.mathcing.domain.id;

import com.aliens.backend.mathcing.domain.MatchingRound;
import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class MatchingApplicationId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_round")
    private MatchingRound matchingRound;

    @Column(name = "member_id")
    private Long memberId;

    protected MatchingApplicationId() {
    }

    public MatchingApplicationId(final MatchingRound matchingRound, final Long memberId) {
        this.matchingRound = matchingRound;
        this.memberId = memberId;
    }

    public MatchingRound getMatchingRound() {
        return matchingRound;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return "MatchingApplicationId{" +
                "matchingRound=" + matchingRound +
                ", memberId=" + memberId +
                '}';
    }
}
