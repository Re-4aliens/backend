package com.aliens.backend.mathcing.validator;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.domain.MatchingResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchingServiceValidator {
    public void checkHasApplied(List<MatchingResult> matchingResults) {
        if (matchingResults.isEmpty()) {
            throw new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO);
        }
    }
}
