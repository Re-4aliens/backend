package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.domain.Block;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.ArrayList;
import java.util.List;

public record Participant(
        Member member,
        Language firstPreferLanguage,
        Language secondPreferLanguage,
        List<Partner> partners,
        PreviousPartnerGroup previousPartnerGroup,
        BlockedPartnerGroup blockedPartnerGroup
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
                                   final List<MatchingResult> previousMatchingResults,
                                   final List<Block> blockHistories) {
        PreviousPartnerGroup previousPartnerGroup = PreviousPartnerGroup.from(previousMatchingResults);
        BlockedPartnerGroup blockedPartnerGroup = BlockedPartnerGroup.from(blockHistories);

        return new Participant(
                matchingApplication.getMember(),
                matchingApplication.getFirstPreferLanguage(),
                matchingApplication.getSecondPreferLanguage(),
                new ArrayList<>(), previousPartnerGroup, blockedPartnerGroup
        );
    }

    public int getNumberOfPartners() {
        return partners.size();
    }

    public void addPartner(Relationship relationship, Participant participant) {
        partners.add(Partner.of(relationship, participant.member));
    }

    public boolean isPartnerWith(Participant participant) {
        for (Partner partner : partners) {
            if (partner.member().equals(participant.member())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMetPreviousRound(Participant participant) {
        return previousPartnerGroup.contains(participant);
    }

    public boolean hasBlocked(Participant participant) {
        return blockedPartnerGroup.contains(participant);
    }
}
