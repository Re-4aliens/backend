package com.aliens.backend.mathcing.validator;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.domain.MatchingRound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MatchingApplicationValidator {
    public boolean canApplyMatch(MatchingRound matchingRound, LocalDateTime now) {
        if (now.isAfter(matchingRound.getMatchingRequestStartTime()) &&
                now.isBefore(matchingRound.getMatchingRequestEndTime())) {
            return true;
        }
        throw new RestApiException(MatchingError.NOT_VALID_MATCHING_TIME);
    }
}
