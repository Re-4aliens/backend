package com.aliens.backend.mathcing.service;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.mathcing.business.model.MatchingResultGroup;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.event.MatchingEventPublisher;
import com.aliens.backend.notification.service.FcmSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class MatchingRoundService {
    private final MatchingRoundRepository matchingRoundRepository;
    private final MatchingResultRepository matchingResultRepository;
    private final MatchingTimeProperties matchingTimeProperties;
    private final MatchingEventPublisher eventPublisher;
    private final FcmSender fcmSender;
    private final Clock clock;

    public MatchingRoundService(final MatchingRoundRepository matchingRoundRepository,
                                final MatchingResultRepository matchingResultRepository,
                                final MatchingTimeProperties matchingTimeProperties,
                                final MatchingEventPublisher eventPublisher, FcmSender fcmSender,
                                final Clock clock) {
        this.matchingRoundRepository = matchingRoundRepository;
        this.matchingResultRepository = matchingResultRepository;
        this.matchingTimeProperties = matchingTimeProperties;
        this.eventPublisher = eventPublisher;
        this.fcmSender = fcmSender;
        this.clock = clock;
    }

    @Scheduled(cron = "${matching.round.update-date}")
    @Transactional
    public void saveMatchRound() {
        MatchingRound matchingRound = MatchingRound.from(LocalDateTime.now(clock), matchingTimeProperties);
        matchingRoundRepository.save(matchingRound);

        sendMatchedNotification();
    }

    private void sendMatchedNotification() {
        List<MatchingResult> previousMatchingResults = findPreviousMatchingResults();
        MatchingResultGroup matchingResultGroup = MatchingResultGroup.of(previousMatchingResults);
        Set<Member> matchedMemberSet = matchingResultGroup.getMatchedMemberSet();
        if (!matchedMemberSet.isEmpty()) {
            fcmSender.sendMatchedNotification(matchedMemberSet);
        }
    }

    private List<MatchingResult> findPreviousMatchingResults() {
        MatchingRound currentRound = findCurrentRound();
        Long previousRound = currentRound.getPreviousRound();
        return matchingResultRepository.findAllByRound(previousRound);
    }

    private MatchingRound findCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }
}
