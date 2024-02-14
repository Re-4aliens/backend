package com.aliens.backend.mathcing.controller.dto.response;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.business.model.Language;

public record MatchingApplicationResponse(
        Long matchingRound,
        Long memberId,
        Language firstPreferLanguage,
        Language secondPreferLanguage) {
    public static MatchingApplicationResponse of(MatchingApplication matchingApplication) {
        return new MatchingApplicationResponse(
                matchingApplication.getMatchingRound().getRound(),
                matchingApplication.getMemberId(),
                matchingApplication.getFirstPreferLanguage(),
                matchingApplication.getSecondPreferLanguage());
    }
}