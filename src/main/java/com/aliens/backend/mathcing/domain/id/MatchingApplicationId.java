package com.aliens.backend.mathcing.domain.id;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.mathcing.domain.MatchingRound;
import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class MatchingApplicationId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_round")
    private MatchingRound matchingRound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected MatchingApplicationId() {
    }

    private MatchingApplicationId(final MatchingRound matchingRound, final Member member) {
        this.matchingRound = matchingRound;
        this.member = member;
    }

    public MatchingRound getMatchingRound() {
        return matchingRound;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public static MatchingApplicationId of(final MatchingRound matchingRound, final Member member) {
        return new MatchingApplicationId(matchingRound, member);
    }

    @Override
    public String toString() {
        return "MatchingApplicationId{" +
                "matchingRound=" + matchingRound +
                ", memberId=" + member +
                '}';
    }
}
