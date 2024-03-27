package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.List;

public class MatchingResultGroup {
    private final List<MatchingResult> matchingResults;

    private MatchingResultGroup(final List<MatchingResult> matchingResults) {
        this.matchingResults = matchingResults;
    }

    public static MatchingResultGroup of(List<MatchingResult> matchingResults) {
        return new MatchingResultGroup(matchingResults);
    }

    public List<MatchingResult> getMatchingResultsWith(MatchingApplication matchingApplication) {
        List<MatchingResult> filteredMatchingResult = matchingResults.stream()
                .filter(matchingResult -> matchingApplication.getMemberId().equals(matchingResult.getMatchingMemberId()))
                .toList();
        return filteredMatchingResult;
    }
}
