package com.aliens.backend.mathcing.domain;

import com.aliens.backend.global.property.MatchingTimeProperties;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Entity
public class MatchingRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round")
    private Long round;

    @Column
    private LocalDateTime requestStartTime;

    @Column
    private LocalDateTime requestEndTime;

    @Column
    private LocalDateTime validStartTime;

    @Column
    private LocalDateTime validEndTime;

    protected MatchingRound() {
    }

    public Long getRound() {
        return round;
    }

    public LocalDateTime getValidStartTime() {
        return validStartTime;
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
        LocalDateTime requestEndTime = requestStartTime.plusHours(matchingTimeProperties.getRequestAvailableTime(dayOfWeek));
        LocalDateTime validStartTime = requestEndTime.plusHours(matchingTimeProperties.getRestrictedTime());
        LocalDateTime validEndTime = validStartTime.plusHours(matchingTimeProperties.getMatchingValidHours(dayOfWeek));
        return new MatchingRound(requestStartTime, requestEndTime, validStartTime, validEndTime);
    }

    public boolean isReceptionTime(LocalDateTime now) {
        return (now.isAfter(requestStartTime) || now.isEqual(requestStartTime)) && now.isBefore(requestEndTime);
    }

    public Long getPreviousRound() {
        return round - 1;
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

    public boolean isFirstTime() {
        return round == 1;
    }
}
