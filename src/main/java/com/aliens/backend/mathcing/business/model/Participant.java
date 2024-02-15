package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.ArrayList;
import java.util.List;

public record Participant(
        Long memberId,
        Language firstPreferLanguage,
        Language secondPreferLanguage,
        List<Partner> partners,
        PreviousPartnerGroup previousPartnerGroup
        // TODO : List<Long> blockedPartners
) {
    public Language getPreferLanguage(MatchingMode matchingMode) {
        if (matchingMode.equals(MatchingMode.FIRST_PREFER_LANGUAGE)) {
            return firstPreferLanguage;
        }
        if (matchingMode.equals(MatchingMode.SECOND_PREFER_LANGUAGE)) {
            return secondPreferLanguage;
        }
        throw new RestApiException(MatchingError.NOT_FOUND_PREFER_LANGUAGE);
    }

    public static Participant from(final MatchingApplication matchingApplication,
                                   final List<MatchingResult> previousMatchingResults) {
        PreviousPartnerGroup previousPartnerGroup = PreviousPartnerGroup.from(previousMatchingResults);
        return new Participant(
                matchingApplication.getMemberId(),
                matchingApplication.getFirstPreferLanguage(),
                matchingApplication.getSecondPreferLanguage(),
                new ArrayList<>(), previousPartnerGroup
        );
    }

    public int getNumberOfPartners() {
        return partners.size();
    }

    public void addPartner(Relationship relationship, Long memberId) {
        partners.add(Partner.of(relationship, memberId));
    }

    public boolean isPartnerWith(Participant participant) {
        for (Partner partner : partners) {
            if (partner.memberId().equals(participant.memberId())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMetPreviousRound(Participant participant) {
        return previousPartnerGroup.contains(participant);
    }
}
