package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class MatchingTimeProperties {
    @Value("${matching.request.time.tuesday.hours}")
    private Long tuesdayRequestAvailableHours;

    @Value("${matching.request.time.friday.hours}")
    private Long fridayRequestAvailableHours;

    @Value("${matching.valid.time.tuesday.hours}")
    private Long tuesdayMatchingValidHours;

    @Value("${matching.valid.time.friday.hours}")
    private Long fridayMatchingValidHours;

    @Value("${matching.round.restricted}")
    private Long restrictedTime;

    @Value("${matching.valid.time.default.hours}")
    private Long defaultMatchingValidHours;

    @Value("${matching.request.time.default.hours}")
    private Long defaultRequestAvailableHours;

    public Long getRequestAvailableTime(DayOfWeek dayOfWeek) {
        if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
            return tuesdayRequestAvailableHours;
        }
        if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
            return fridayRequestAvailableHours;
        }
        return defaultRequestAvailableHours;
    }

    public Long getMatchingValidHours(final DayOfWeek dayOfWeek) {
        if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
            return tuesdayMatchingValidHours;
        }
        if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
            return fridayMatchingValidHours;
        }
        return defaultMatchingValidHours;
    }

    public Long getRestrictedTime() {
        return restrictedTime;
    }
}
