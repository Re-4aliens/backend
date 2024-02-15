package com.aliens.backend.mathcing.controller.dto.request;

import com.aliens.backend.block.domain.Block;
import com.aliens.backend.mathcing.business.model.BlockHistoryGroup;
import com.aliens.backend.mathcing.business.model.MatchingResultGroup;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.List;

public record MatchingOperateRequest(
        List<MatchingApplication> matchingApplications,
        MatchingResultGroup previousMatchingResultGroup,
        BlockHistoryGroup blockHistoryGroup
) {
    public static MatchingOperateRequest of(List<MatchingApplication> matchingApplications,
                                            List<MatchingResult> previousMatchingResults,
                                            List<Block> blockHistories) {
        return new MatchingOperateRequest(matchingApplications,
                MatchingResultGroup.of(previousMatchingResults),
                BlockHistoryGroup.of(blockHistories));
    }
}