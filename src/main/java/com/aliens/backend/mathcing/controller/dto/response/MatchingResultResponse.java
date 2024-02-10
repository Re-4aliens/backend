package com.aliens.backend.mathcing.controller.dto.response;

import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.service.model.Relationship;

public record MatchingResultResponse(
        Long matchedMemberId,
        Relationship relationship
) {
    public static MatchingResultResponse from(MatchingResult matchingResult) {
        return new MatchingResultResponse(matchingResult.getMatchedMemberId(), matchingResult.getRelationship());
    }
}
