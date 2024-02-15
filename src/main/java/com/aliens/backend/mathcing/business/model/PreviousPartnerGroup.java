package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.List;

public class PreviousPartnerGroup {
    private final List<Long> previousPartners;

    private PreviousPartnerGroup(final List<Long> previousPartners) {
        this.previousPartners = previousPartners;
    }

    public static PreviousPartnerGroup from(final List<MatchingResult> previousMatchingResults) {
        List<Long> previousPartners = previousMatchingResults.stream()
                .mapToLong(MatchingResult::getMatchedMemberId).boxed().toList();
        return new PreviousPartnerGroup(previousPartners);
    }

    public boolean contains(Participant participant) {
        return previousPartners.contains(participant.memberId());
    }

    @Override
    public String toString() {
        return "PreviousPartnerGroup{" +
                "previousPartners=" + previousPartners +
                '}';
    }
}
