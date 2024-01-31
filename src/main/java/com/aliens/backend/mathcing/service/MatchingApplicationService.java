package com.aliens.backend.mathcing.service;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.aliens.backend.mathcing.controller.dto.request.MatchingRequest.*;

@Service
public class MatchingApplicationService {
    private MatchingApplicationRepository matchingApplicationRepository;
    private MatchingRoundRepository matchingRoundRepository;

    @Autowired
    public MatchingApplicationService(final MatchingApplicationRepository matchingApplicationRepository,
                                      final MatchingRoundRepository matchingRoundRepository) {
        this.matchingApplicationRepository = matchingApplicationRepository;
        this.matchingRoundRepository = matchingRoundRepository;
    }

    @Transactional
    public void saveParticipant(final MatchingApplicationRequest matchingApplicationRequest) {
        MatchingRound currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
        matchingApplicationRepository.save(matchingApplicationRequest.toEntity(currentRound));
    }
}
