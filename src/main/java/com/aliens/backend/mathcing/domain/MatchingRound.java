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

    @Column(name = "request_start_time")
    private LocalDateTime requestStartTime;

    @Column(name = "request_end_time")
    private LocalDateTime requestEndTime;

    @Column(name = "valid_start_time")
    private LocalDateTime validStartTime;

    @Column(name = "valid_end_time")
    private LocalDateTime validEndTime;

    protected MatchingRound() {
    }

    public Long getRound() {
        return round;
    }

    public LocalDateTime getRequestStartTime() {
        return requestStartTime;
    }

    public LocalDateTime getRequestEndTime() {
        return requestEndTime;
    }

    public LocalDateTime getValidStartTime() {
        return validStartTime;
    }

    public LocalDateTime getValidEndTime() {
        return validEndTime;
    }

    public DayOfWeek getDayOfWeek() {
        return requestStartTime.getDayOfWeek();
    }

    private MatchingRound(final LocalDateTime requestStartTime,
                         final LocalDateTime requestEndTime,
                         final LocalDateTime validStartTime,
                         final LocalDateTime validEndTime) {
        this.requestStartTime = requestStartTime;
        this.requestEndTime = requestEndTime;
        this.validStartTime = validStartTime;
        this.validEndTime = validEndTime;
    }

    public static MatchingRound from(final LocalDateTime today, final MatchingTimeProperties matchingTimeProperties) {
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        LocalDateTime requestStartTime = today.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime requestEndTime = requestStartTime.plusHours(matchingTimeProperties.getRequestAvailableTime());
        LocalDateTime validStartTime = today.withHour(matchingTimeProperties.getValidBeginHours()).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime validEndTime = validStartTime.plusHours(matchingTimeProperties.getMatchingValidHours(dayOfWeek));

        return new MatchingRound(requestStartTime, requestEndTime, validStartTime, validEndTime);
    }

    public boolean isReceptionTime(LocalDateTime now) {
        return now.isAfter(this.getRequestStartTime()) && now.isBefore(this.getRequestEndTime());
    }

    @Override
    public String toString() {
        return "MatchingRound{" +
                "round=" + round +
                ", requestStartTime=" + requestStartTime +
                ", requestEndTime=" + requestEndTime +
                ", validStartTime=" + validStartTime +
                ", validEndTime=" + validEndTime +
                '}';
    }
}
