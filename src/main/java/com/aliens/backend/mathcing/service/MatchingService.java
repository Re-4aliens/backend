package com.aliens.backend.mathcing.service;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.business.MatchingBusiness;
import com.aliens.backend.mathcing.controller.dto.response.MatchingResponse;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.model.Participant;
import com.aliens.backend.mathcing.service.model.Partner;
import com.aliens.backend.mathcing.validator.MatchingServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.aliens.backend.mathcing.controller.dto.response.MatchingResponse.*;

@Service
public class MatchingService {
    private final MatchingRoundRepository matchingRoundRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final MatchingResultRepository matchingResultRepository;
    private final MatchingBusiness matchingBusiness;
    private final MatchingServiceValidator matchingServiceValidator;

    private MatchingRound currentRound;

    @Autowired
    public MatchingService(final MatchingRoundRepository matchingRoundRepository,
                           final MatchingApplicationRepository matchingApplicationRepository,
                           final MatchingResultRepository matchingResultRepository,
                           final MatchingBusiness matchingBusiness,
                           final MatchingServiceValidator matchingServiceValidator) {
        this.matchingRoundRepository = matchingRoundRepository;
        this.matchingApplicationRepository = matchingApplicationRepository;
        this.matchingResultRepository = matchingResultRepository;
        this.matchingBusiness = matchingBusiness;
        this.matchingServiceValidator = matchingServiceValidator;
    }

    @Scheduled(cron = "${matching.round.start}")
    @Transactional
    public void operateMatching() {
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
        List<Participant> participants = matchingBusiness.operateMatching(
                matchingApplicationRepository.findAllByMatchingRound(currentRound));
        saveMatchingResult(participants);
    }

    @Transactional(readOnly = true)
    public List<MatchingResultResponse> findMatchingResult(final Long memberId) {
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
        List<MatchingResult> matchingResults =
                matchingResultRepository.findAllByMatchingRoundAndMemberId(currentRound, memberId);
        matchingServiceValidator.checkHasApplied(matchingResults);
        return matchingResults.stream().map(MatchingResultResponse::of).toList();
    }

    private void saveMatchingResult(final List<Participant> participants) {
        for (Participant participant : participants) {
            Long memberId = participant.memberId();
            for (Partner partner : participant.partners()) {
                matchingResultRepository.save(
                        MatchingResult.of(currentRound, memberId, partner.memberId(), partner.relationship()));
            }
            // TODO : 매칭 완료 알림 이벤트 발송 & 채팅방 개설 이벤트 발송
        }
    }
}
