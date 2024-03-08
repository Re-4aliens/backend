package com.aliens.backend.mathcing.controller.dto.response;

import com.aliens.backend.mathcing.domain.MatchingRound;

import java.time.LocalDateTime;

public record MatchingBeginTimeResponse(
    Long round,
    LocalDateTime matchingBeginTime
) {
    public static MatchingBeginTimeResponse from(MatchingRound matchingRound) {
        return new MatchingBeginTimeResponse(matchingRound.getRound(), matchingRound.getValidStartTime());
    }
}
