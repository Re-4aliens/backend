package com.aliens.backend.mathcing.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.domain.Block;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
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
        List<MatchingResult> matchingResults = getMatchingResults(currentRound, loginMember);
        checkHasApplied(matchingResults);
        return matchingResults.stream().map(MatchingResultResponse::from).toList();
    }

    @Scheduled(cron = "${matching.round.end}")
    @Transactional
    public void expireMatching() {
        List<MatchingResult> previousMatchingResults = getPreviousMatchingResults();
        previousMatchingResults.forEach(this::resetMatch); // TODO : 채팅방 폐쇄
    }

    private void saveMatchingResult(final MatchingRound matchingRound, final List<Participant> participants) {
        for (Participant participant : participants) {
            for (Partner partner : participant.partners()) {
                MatchingResult matchingResult = MatchingResult.from(matchingRound, participant, partner);
                matchBetween(matchingResult);
            }
            // TODO : 채팅방 개설 이벤트 발송
        }
        // TODO : 매칭 완료 이벤트 발송
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

    private List<MatchingResult> getMatchingResults(final MatchingRound matchingRound, final LoginMember loginMember) {
        return matchingResultRepository.findAllByMatchingRoundAndMemberId(matchingRound, loginMember.memberId());
    }

    private List<MatchingApplication> getMatchingApplications(final MatchingRound matchingRound) {
        return matchingApplicationRepository.findAllByMatchingRound(matchingRound);
    }

    private List<MatchingResult> getPreviousMatchingResults() {
        MatchingRound currentRound = getCurrentRound();
        Long previousRound = currentRound.getPreviousRound();
        return matchingResultRepository.findAllByRound(previousRound);
    }

    private List<Block> getBlockListByMatchingApplications(final List<MatchingApplication> matchingApplications) {
        List<Block> blockHistory = matchingApplications.stream()
                .map(MatchingApplication::getMember)
                .flatMap(member -> getBlockListByBlockingMembers(member).stream())
                .toList();
        return blockHistory;
    }

    private List<Block> getBlockListByBlockingMembers(final Member blockingMember) {
        return blockRepository.findAllByBlockingMember(blockingMember);
    }

    private void matchBetween(final MatchingResult matchingResult) {
        matchingResultRepository.save(matchingResult);
        Member matchingMember = matchingResult.getMatchingMember();
        Member matchedMember = matchingResult.getMatchedMember();
        matchingMember.toMatched();
        matchedMember.toMatched();
    }

    private void resetMatch(MatchingResult matchingResult) {
        Member matchingMember = matchingResult.getMatchingMember();
        Member matchedMember = matchingResult.getMatchedMember();
        matchingMember.toPrevious();
        matchedMember.toPrevious();
    }

    private MatchingOperateRequest createOperateRequest(final MatchingRound matchingRound) {
        List<MatchingApplication> matchingApplications = getMatchingApplications(matchingRound);
        List<MatchingResult> previousMatchingResult = getPreviousMatchingResults();
        List<Block> participantBlockHistory = getBlockListByMatchingApplications(matchingApplications);
        return MatchingOperateRequest.of(matchingApplications, previousMatchingResult, participantBlockHistory);
    }
}
