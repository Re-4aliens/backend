package com.aliens.backend.mathcing.controller.dto.response;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.service.model.Language;
import com.aliens.backend.mathcing.service.model.Partner;
import com.aliens.backend.mathcing.service.model.Relationship;

import java.util.List;

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

    public record MatchingResultResponse(
            Long matchedMemberId,
            Relationship relationship
    ) {
        public static MatchingResultResponse of(MatchingResult matchingResult) {
            return new MatchingResultResponse(matchingResult.getId().getMatchedMemberId(), matchingResult.getRelationship());
        }
    }
}
