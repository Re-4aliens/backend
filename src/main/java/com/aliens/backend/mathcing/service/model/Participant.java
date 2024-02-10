package com.aliens.backend.mathcing.service.model;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.domain.MatchingApplication;

import java.util.ArrayList;
import java.util.List;

public record Participant(
        Long memberId,
        Language firstPreferLanguage,
        Language secondPreferLanguage,
        List<Partner> partners
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

    public static Participant of(final MatchingApplication matchingApplication) {
        return new Participant(
                matchingApplication.getMemberId(),
                matchingApplication.getFirstPreferLanguage(),
                matchingApplication.getSecondPreferLanguage(),
                new ArrayList<>()
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
}
