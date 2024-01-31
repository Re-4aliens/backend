package com.aliens.backend.mathcing.domain;

import com.aliens.backend.global.property.MatchingTimeProperties;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Entity
public class MatchingRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long round;

    @Column(name = "matching_request_start_time")
    private LocalDateTime matchingRequestStartTime;

    @Column(name = "matching_request_end_time")
    private LocalDateTime matchingRequestEndTime;

    @Column(name = "matching_valid_start_time")
    private LocalDateTime matchingValidStartTime;

    @Column(name = "matching_valid_end_time")
    private LocalDateTime matchingValidEndTime;

    protected MatchingRound() {
    }

    public Long getRound() {
        return round;
    }

    public LocalDateTime getMatchingRequestStartTime() {
        return matchingRequestStartTime;
    }

    public LocalDateTime getMatchingRequestEndTime() {
        return matchingRequestEndTime;
    }

    public LocalDateTime getMatchingValidStartTime() {
        return matchingValidStartTime;
    }

    public LocalDateTime getMatchingValidEndTime() {
        return matchingValidEndTime;
    }

    public MatchingRound(final LocalDateTime matchingRequestStartTime,
                         final LocalDateTime matchingRequestEndTime,
                         final LocalDateTime matchingValidStartTime,
                         final LocalDateTime matchingValidEndTime) {
        this.matchingRequestStartTime = matchingRequestStartTime;
        this.matchingRequestEndTime = matchingRequestEndTime;
        this.matchingValidStartTime = matchingValidStartTime;
        this.matchingValidEndTime = matchingValidEndTime;
    }

    public static MatchingRound of(LocalDateTime today, MatchingTimeProperties matchingTimeProperties) {
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        LocalDateTime matchingRequestStartTime = today.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime matchingRequestEndTime = matchingRequestStartTime.plusHours(matchingTimeProperties.getMatchingRequestAvailableTime());
        LocalDateTime matchingValidStartTime = today.withHour(matchingTimeProperties.getMatchingValidBeginHours()).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime matchingValidEndTime = matchingValidStartTime.plusHours(matchingTimeProperties.getMatchingValidHours(dayOfWeek));

        return new MatchingRound(matchingRequestStartTime, matchingRequestEndTime, matchingValidStartTime, matchingValidEndTime);
    }
}
