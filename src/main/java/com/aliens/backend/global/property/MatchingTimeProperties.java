package com.aliens.backend.global.property;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class MatchingTimeProperties {
    @Value("${matching.request.time.hours}")
    private String matchingRequestAvailableTime;

    @Value("${matching.valid.time.hours}")
    private String matchingValidBeginHours;

    @Value("${matching.valid.day-of-week.if.monday.hours}")
    private String mondayMatchingValidHours;

    @Value("${matching.valid.day-of-week.if.thursday.hours}")
    private String thursdayMatchingValidHours;

    @Value("${matching.valid.day-of-week.if.default.hours}")
    private String defaultMatchingValidHours;

    public Long getMatchingRequestAvailableTime() {
        return Long.parseLong(matchingRequestAvailableTime);
    }

    public Integer getMatchingValidBeginHours() {
        return Integer.parseInt(matchingValidBeginHours);
    }

    public Long getMatchingValidHours(final DayOfWeek dayOfWeek) {
        if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
            return Long.parseLong(mondayMatchingValidHours);
        }
        if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
            return Long.parseLong(thursdayMatchingValidHours);
        }
        return Long.parseLong(defaultMatchingValidHours);
    }
}
