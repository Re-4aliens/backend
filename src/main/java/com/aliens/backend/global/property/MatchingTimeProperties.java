package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class MatchingTimeProperties {
    @Value("${matching.request.time.hours}")
    private Long requestAvailableTime;

    @Value("${matching.valid.time.hours}")
    private Integer validBeginHours;

    @Value("${matching.valid.day-of-week.if.monday.hours}")
    private Long mondayMatchingValidHours;

    @Value("${matching.valid.day-of-week.if.thursday.hours}")
    private Long thursdayMatchingValidHours;

    @Value("${matching.valid.day-of-week.if.default.hours}")
    private Long defaultMatchingValidHours;

    public Long getRequestAvailableTime() {
        return requestAvailableTime;
    }

    public Integer getValidBeginHours() {
        return validBeginHours;
    }

    public Long getMatchingValidHours(final DayOfWeek dayOfWeek) {
        if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
            return mondayMatchingValidHours;
        }
        if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
            return thursdayMatchingValidHours;
        }
        return defaultMatchingValidHours;
    }
}
