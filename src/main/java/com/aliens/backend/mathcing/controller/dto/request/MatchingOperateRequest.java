package com.aliens.backend.mathcing.controller.dto.request;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.List;

public record MatchingOperateRequest(
        List<MatchingApplication> matchingApplications,
        List<MatchingResult> previousMatchingResults
        // TODO : 차단 유저 리스트
) {
    public static MatchingOperateRequest of(List<MatchingApplication> matchingApplications,
                                            List<MatchingResult> previousMatchingResults) {
        return new MatchingOperateRequest(matchingApplications, previousMatchingResults);
    }
}
