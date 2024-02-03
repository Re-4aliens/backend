package com.aliens.backend.mathcing.service.model;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;

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

    public int getNumberOfPartners() {
        return partners.size();
    }

    public void addPartner(Relationship relationship, Participant participant) {
        partners.add(Partner.of(relationship, participant));
    }

    public boolean isPartnerWith(Participant participant) {
        return partners.stream()
                .anyMatch(partner -> partner.participant().equals(participant));
    }
}
