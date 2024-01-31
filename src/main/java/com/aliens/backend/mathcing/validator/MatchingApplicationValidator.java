package com.aliens.backend.mathcing.validator;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.domain.MatchingRound;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MatchingApplicationValidator {
    public boolean canApplyMatching(MatchingRound matchingRound) {
        LocalDateTime now = LocalDateTime.now();
        if (matchingRound.getMatchingRequestStartTime().isAfter(now) &&
                matchingRound.getMatchingRequestEndTime().isBefore(now)) {
            return true;
        }
        throw new RestApiException(MatchingError.NOT_VALID_MATCHING_TIME);
    }
}
