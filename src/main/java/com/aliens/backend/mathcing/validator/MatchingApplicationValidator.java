package com.aliens.backend.mathcing.validator;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.domain.MatchingRound;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MatchingApplicationValidator {
    public void checkReceptionTime(MatchingRound matchingRound, LocalDateTime now) {
        if (!isMatchingReceptionTime(matchingRound, now)) {
            throw new RestApiException(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME);
        }
    }

    private boolean isMatchingReceptionTime(MatchingRound matchingRound, LocalDateTime now) {
        return now.isAfter(matchingRound.getMatchingRequestStartTime()) &&
                now.isBefore(matchingRound.getMatchingRequestEndTime());
    }
}
