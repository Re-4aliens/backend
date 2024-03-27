package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.List;

public class PreviousPartnerGroup {
    private final List<Member> previousPartners;

    private PreviousPartnerGroup(final List<Member> previousPartners) {
        this.previousPartners = previousPartners;
    }

    public static PreviousPartnerGroup from(final List<MatchingResult> previousMatchingResults) {
        List<Member> previousPartners = previousMatchingResults.stream()
                .map(MatchingResult::getMatchedMember).toList();
        return new PreviousPartnerGroup(previousPartners);
    }

    public boolean contains(Participant participant) {
        return previousPartners.contains(participant.member());
    }

    @Override
    public String toString() {
        return "PreviousPartnerGroup{" +
                "previousPartners=" + previousPartners +
                '}';
    }
}
