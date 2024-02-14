package com.aliens.backend.mathcing.service;

import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class MatchingRoundService {
    private final MatchingRoundRepository matchingRoundRepository;
    private final MatchingTimeProperties matchingTimeProperties;
    private final Clock clock;

    public MatchingRoundService(final MatchingRoundRepository matchingRoundRepository,
                                final MatchingTimeProperties matchingTimeProperties,
                                final Clock clock) {
        this.matchingRoundRepository = matchingRoundRepository;
        this.matchingTimeProperties = matchingTimeProperties;
        this.clock = clock;
    }

    @Scheduled(cron = "${matching.round.update-date}")
    private void saveMatchRound() {
        MatchingRound matchingRound = MatchingRound.from(LocalDateTime.now(clock), matchingTimeProperties);
        matchingRoundRepository.save(matchingRound);
    }
}
