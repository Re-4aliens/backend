package com.aliens.backend.mathcing.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.business.MatchingBusiness;
import com.aliens.backend.mathcing.controller.dto.response.MatchingResultResponse;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.model.Participant;
import com.aliens.backend.mathcing.service.model.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchingService {
    private final MatchingRoundRepository matchingRoundRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final MatchingResultRepository matchingResultRepository;
    private final MatchingBusiness matchingBusiness;

    public MatchingService(final MatchingRoundRepository matchingRoundRepository,
                           final MatchingApplicationRepository matchingApplicationRepository,
                           final MatchingResultRepository matchingResultRepository,
                           final MatchingBusiness matchingBusiness) {
        this.matchingRoundRepository = matchingRoundRepository;
        this.matchingApplicationRepository = matchingApplicationRepository;
        this.matchingResultRepository = matchingResultRepository;
        this.matchingBusiness = matchingBusiness;
    }

    @Scheduled(cron = "${matching.round.start}")
    @Transactional
    public void operateMatching() {
        MatchingRound currentRound = getCurrentRound();
        List<MatchingApplication> matchingApplications = getMatchingApplications(currentRound);
        matchingBusiness.operateMatching(matchingApplications);

        List<Participant> matchedParticipants = matchingBusiness.getParticipants();
        saveMatchingResult(currentRound, matchedParticipants);
    }

    @Transactional(readOnly = true)
    public List<MatchingResultResponse> findMatchingResult(final LoginMember loginMember) {
        MatchingRound currentRound = getCurrentRound();
        List<MatchingResult> matchingResults = getMatchingResult(currentRound, loginMember);
        checkHasApplied(matchingResults);
        return matchingResults.stream().map(MatchingResultResponse::from).toList();
    }

    private void saveMatchingResult(final MatchingRound matchingRound, final List<Participant> participants) {
        for (Participant participant : participants) {
            for (Partner partner : participant.partners()) {
                MatchingResult matchingResult =
                        MatchingResult.of(matchingRound, participant.memberId(), partner.memberId(), partner.relationship());
                matchingResultRepository.save(matchingResult);
            }
            // TODO : 매칭 완료 알림 이벤트 발송 & 채팅방 개설 이벤트 발송
        }
    }

    private void checkHasApplied(final List<MatchingResult> matchingResults) {
        if (matchingResults.isEmpty()) {
            throw new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO);
        }
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private List<MatchingResult> getMatchingResult(final MatchingRound matchingRound, final LoginMember loginMember) {
        return matchingResultRepository.findAllByMatchingRoundAndMemberId(matchingRound, loginMember.memberId());
    }

    private List<MatchingApplication> getMatchingApplications(final MatchingRound matchingRound) {
        return matchingApplicationRepository.findAllByMatchingRound(matchingRound);
    }
}
