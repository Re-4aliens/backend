package com.aliens.backend.mathcing.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.block.domain.Block;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.mathcing.business.MatchingBusiness;
import com.aliens.backend.mathcing.controller.dto.request.MatchingOperateRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingResultResponse;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.business.model.Participant;
import com.aliens.backend.mathcing.business.model.Partner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchingProcessService {
    private final MatchingBusiness matchingBusiness;
    private final MatchingRoundRepository matchingRoundRepository;
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final MatchingResultRepository matchingResultRepository;
    private final BlockRepository blockRepository;

    public MatchingProcessService(final MatchingBusiness matchingBusiness,
                                  final MatchingRoundRepository matchingRoundRepository,
                                  final MatchingApplicationRepository matchingApplicationRepository,
                                  final MatchingResultRepository matchingResultRepository,
                                  final BlockRepository blockRepository) {
        this.matchingRoundRepository = matchingRoundRepository;
        this.matchingApplicationRepository = matchingApplicationRepository;
        this.matchingResultRepository = matchingResultRepository;
        this.matchingBusiness = matchingBusiness;
        this.blockRepository = blockRepository;
    }

    @Scheduled(cron = "${matching.round.start}")
    @Transactional
    public void operateMatching() {
        MatchingRound currentRound = getCurrentRound();
        MatchingOperateRequest matchingOperateRequest = createOperateRequest(currentRound);

        matchingBusiness.operateMatching(matchingOperateRequest);

        List<Participant> matchedParticipants = matchingBusiness.getMatchedParticipants();
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
    
    private MatchingRound getPreviousMatchingRound(MatchingRound matchingRound) {
        Long previousRound = matchingRound.getPreviousRound();
        return matchingRoundRepository.findMatchingRoundByRound(previousRound)
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private List<MatchingResult> getPreviousMatchingResult(MatchingRound matchingRound) {
        if (matchingRound.isFirstRound()) {
            return new ArrayList<>();
        }
        MatchingRound previousMatchingRound = getPreviousMatchingRound(matchingRound);
        return matchingResultRepository.findAllByMatchingRound(previousMatchingRound);
    }

    private List<Block> getBlockListByMatchingApplications(MatchingRound matchingRound) {
        List<MatchingApplication> matchingApplications = getMatchingApplications(matchingRound);
        List<Block> blockHistory = matchingApplications.stream()
                .map(MatchingApplication::getMember)
                .flatMap(member -> getBlockListByBlockingMember(member).stream())
                .toList();
        return blockHistory;
    }

    private List<Block> getBlockListByBlockingMember(Member blockingMember) {
        return blockRepository.findAllByBlockingMember(blockingMember);
    }

    private MatchingOperateRequest createOperateRequest(MatchingRound matchingRound) {
        List<MatchingApplication> matchingApplications = getMatchingApplications(matchingRound);
        List<MatchingResult> previousMatchingResult = getPreviousMatchingResult(matchingRound);
        List<Block> participantBlockHistory = getBlockListByMatchingApplications(matchingRound);
        return MatchingOperateRequest.of(matchingApplications, previousMatchingResult, participantBlockHistory);
    }
}
