package com.aliens.backend.mathcing.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingApplicationResponse;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.id.MatchingApplicationId;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class MatchingApplicationService {
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final MatchingRoundRepository matchingRoundRepository;
    private final Clock clock;

    public MatchingApplicationService(final MatchingApplicationRepository matchingApplicationRepository,
                                      final MatchingRoundRepository matchingRoundRepository,
                                      final Clock clock) {
        this.matchingApplicationRepository = matchingApplicationRepository;
        this.matchingRoundRepository = matchingRoundRepository;
        this.clock = clock;
    }

    @Transactional
    public void saveParticipant(final LoginMember loginMember,
                                final MatchingApplicationRequest matchingApplicationRequest) {
        MatchingRound currentRound = getCurrentRound();
        checkReceptionTime(currentRound);
        MatchingApplication matchingApplication = MatchingApplication.from(loginMember, matchingApplicationRequest, currentRound);
        matchingApplicationRepository.save(matchingApplication);
    }

    @Transactional(readOnly = true)
    public MatchingApplicationResponse findMatchingApplication(final LoginMember loginMember) {
        MatchingRound currentRound = getCurrentRound();
        MatchingApplication matchingApplication = getMatchingApplication(currentRound, loginMember);
        return MatchingApplicationResponse.from(matchingApplication);
    }

    @Transactional
    public void deleteMatchingApplication(final LoginMember loginMember) {
        MatchingRound currentRound = getCurrentRound();
        checkReceptionTime(currentRound);
        MatchingApplication matchingApplication = getMatchingApplication(currentRound, loginMember);
        matchingApplicationRepository.delete(matchingApplication);
    }

    private void checkReceptionTime(MatchingRound matchingRound) {
        if (!matchingRound.isReceptionTime(LocalDateTime.now(clock))) {
            throw new RestApiException(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME);
        }
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private MatchingApplication getMatchingApplication(MatchingRound matchingRound, LoginMember loginMember) {
        return matchingApplicationRepository.findById(MatchingApplicationId.of(matchingRound, loginMember.memberId()))
                .orElseThrow(()->new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO));
    }
}
