package com.aliens.backend.mathcing.controller.dto.response;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.service.model.Language;

public class MatchingResponse {
    public record MatchingApplicationResponse(
            Long matchingRound,
            Long memberId,
            Language firstPreferLanguage,
            Language secondPreferLanguage) {
        public static MatchingApplicationResponse of(MatchingApplication matchingApplication) {
            return new MatchingApplicationResponse(
                    matchingApplication.getId().getMatchingRound().getRound(),
                    matchingApplication.getId().getMemberId(),
                    matchingApplication.getFirstPreferLanguage(),
                    matchingApplication.getSecondPreferLanguage());
        }
    }
}
