package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.List;

public class PreviousPartnerGroup {
    private final List<Long> previousPartners;

    private PreviousPartnerGroup(final List<Long> previousPartners) {
        this.previousPartners = previousPartners;
    }

    public static PreviousPartnerGroup from(final List<MatchingResult> previousMatchingResult) {
        List<Long> previousPartners = previousMatchingResult.stream()
                .mapToLong(MatchingResult::getMatchedMemberId).boxed().toList();
        return new PreviousPartnerGroup(previousPartners);
    }

    public boolean contains(Participant participant) {
        return previousPartners.contains(participant.memberId());
    }
}
