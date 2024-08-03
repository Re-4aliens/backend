package com.aliens.backend.mathcing.domain.id;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.mathcing.domain.MatchingRound;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class MatchingResultId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_round")
    private MatchingRound matchingRound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_member_id")
    private Member matchingMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_member_id")
    private Member matchedMember;

    protected MatchingResultId() {
    }

    private MatchingResultId(final MatchingRound matchingRound, final Member matchingMember, final Member matchedMember) {
        this.matchingRound = matchingRound;
        this.matchingMember = matchingMember;
        this.matchedMember = matchedMember;
    }

    public static MatchingResultId of(final MatchingRound matchingRound,final Member matchingMemberId,final Member matchedMemberId) {
        return new MatchingResultId(matchingRound, matchingMemberId, matchedMemberId);
    }

    public MatchingRound getMatchingRound() {
        return matchingRound;
    }

    public Member getMatchingMember() {
        return matchingMember;
    }

    public Member getMatchedMember() {
        return matchedMember;
    }

    public Long getMatchingMemberId() {
        return matchingMember.getId();
    }

    public Long getMatchedMemberId() {
        return matchedMember.getId();
    }
}
