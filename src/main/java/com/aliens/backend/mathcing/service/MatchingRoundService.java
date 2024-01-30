package com.aliens.backend.mathcing.service;

import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MatchingRoundService {
    private MatchingRoundRepository matchingRoundRepository;
    private MatchingTimeProperties matchingTimeProperties;

    @Autowired
    public MatchingRoundService(final MatchingRoundRepository matchingRoundRepository,
                                final MatchingTimeProperties matchingTimeProperties) {
        this.matchingRoundRepository = matchingRoundRepository;
        this.matchingTimeProperties = matchingTimeProperties;
    }

    @Scheduled(cron = "${matching.round.update-date}")
    private void saveMatchRound() {
        matchingRoundRepository.save(MatchingRound.of(LocalDateTime.now(), matchingTimeProperties));
    }
}
