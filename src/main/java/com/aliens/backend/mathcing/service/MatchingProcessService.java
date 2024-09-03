package com.aliens.backend.mathcing.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.domain.Block;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.chat.domain.ChatParticipant;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.repository.ChatParticipantRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.ChatError;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.mathcing.business.MatchingBusiness;
import com.aliens.backend.mathcing.business.model.Participant;
import com.aliens.backend.mathcing.controller.dto.request.MatchingOperateRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingResultResponse;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.event.MatchingEventPublisher;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
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
    private final MatchingEventPublisher eventPublisher;
    private final ChatParticipantRepository chatParticipantRepository;

    public MatchingProcessService(final MatchingBusiness matchingBusiness,
                                  final MatchingRoundRepository matchingRoundRepository,
                                  final MatchingApplicationRepository matchingApplicationRepository,
                                  final MatchingResultRepository matchingResultRepository,
                                  final BlockRepository blockRepository,
                                  final MatchingEventPublisher eventPublisher,
                                  final ChatParticipantRepository chatParticipantRepository) {
        this.matchingRoundRepository = matchingRoundRepository;
        this.matchingApplicationRepository = matchingApplicationRepository;
        this.matchingResultRepository = matchingResultRepository;
        this.matchingBusiness = matchingBusiness;
        this.blockRepository = blockRepository;
        this.eventPublisher = eventPublisher;
        this.chatParticipantRepository = chatParticipantRepository;
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

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private MatchingOperateRequest createOperateRequest(final MatchingRound matchingRound) {
        List<MatchingApplication> matchingApplications = getMatchingApplications(matchingRound);
        List<MatchingResult> previousMatchingResult = getPreviousMatchingResults();
        List<Block> participantBlockHistory = getBlockListByMatchingApplications(matchingApplications);
        return MatchingOperateRequest.of(matchingApplications, previousMatchingResult, participantBlockHistory);
    }

    private void saveMatchingResult(final MatchingRound matchingRound, final List<Participant> participants) {
        participants.stream()
                .flatMap(participant -> participant.partners().stream()
                        .map(partner -> MatchingResult.from(matchingRound, participant, partner)))
                .forEach(this::matchBetween);

        participants.stream()
                .filter(participant -> !participant.hasPartner())
                .forEach(Participant::expireMatch);

        if (hasMatchedParticipants(participants)) {
            eventPublisher.createChatRoom(participants);
            eventPublisher.sendNotification(participants);
        }
    }

    @Transactional(readOnly = true)
    public List<MatchingResultResponse> findMatchingResult(final LoginMember loginMember) {
        MatchingRound currentRound = getCurrentRound();
        List<MatchingResult> matchingResults = getMatchingResults(currentRound, loginMember);
        return makePartnerInfos(loginMember, matchingResults);
    }

    private List<MatchingResult> getMatchingResults(final MatchingRound matchingRound, final LoginMember loginMember) {
        Long round = matchingRound.getRound();

        if(!matchingRound.isFirstTime()) {
            round = matchingRound.getPreviousRound();
        }

        return matchingResultRepository.findAllByMatchingRoundAndMemberId(round, loginMember.memberId());
    }

    private List<MatchingResultResponse> makePartnerInfos(LoginMember loginMember, List<MatchingResult> matchingResults) {
        return matchingResults.stream()
                .map(result -> {
                    MatchingApplication matchedMemberApplication = findMatchedMemberApplicationByRoundAndMemberId(result);
                    MemberPageResponse matchedMemberPageResponse = result.getMatchedMemberPageResponse();
                    ChatParticipant chatParticipant = getChatParticipant(loginMember, result);
                    ChatRoom chatRoom = chatParticipant.getChatRoom();
                    return MatchingResultResponse.of(result, chatRoom, matchedMemberPageResponse, matchedMemberApplication);
                })
                .toList();
    }

    private MatchingApplication findMatchedMemberApplicationByRoundAndMemberId(MatchingResult result) {
        return matchingApplicationRepository.findByMatchingRoundAndMemberId(result.getMatchingRound(), result.getMatchedMemberId())
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO));
    }

    private ChatParticipant getChatParticipant(final LoginMember loginMember, final MatchingResult result) {
        return chatParticipantRepository.findByMemberIdAndMatchedMemberId(loginMember.memberId(), result.getMatchedMemberId())
                .orElseThrow(() -> new RestApiException(ChatError.CHAT_ROOM_NOT_FOUND));
    }

    @Scheduled(cron = "${matching.round.end}")
    @Transactional
    public void expireMatching() {
        List<MatchingApplication> previousMatchingApplications = getPreviousMatchingApplications();
        previousMatchingApplications.forEach(MatchingApplication::expireMatch);
        eventPublisher.expireChatRoom();
    }

    private boolean hasMatchedParticipants(List<Participant> participants) {
        return !participants.stream().filter(Participant::hasPartner).toList().isEmpty();
    }

    private List<MatchingApplication> getMatchingApplications(final MatchingRound matchingRound) {
        return matchingApplicationRepository.findAllByRound(matchingRound.getRound());
    }

    private List<MatchingResult> getPreviousMatchingResults() {
        MatchingRound currentRound = getCurrentRound();
        Long previousRound = currentRound.getPreviousRound();
        return matchingResultRepository.findAllByRound(previousRound);
    }

    private List<MatchingApplication> getPreviousMatchingApplications() {
        MatchingRound currentRound = getCurrentRound();
        Long previousRound = currentRound.getPreviousRound();
        return matchingApplicationRepository.findAllByRound(previousRound);
    }

    private List<Block> getBlockListByMatchingApplications(final List<MatchingApplication> matchingApplications) {
        return matchingApplications.stream()
                .map(MatchingApplication::getMember)
                .flatMap(member -> getBlockListByBlockingMembers(member).stream())
                .toList();
    }

    private List<Block> getBlockListByBlockingMembers(final Member blockingMember) {
        return blockRepository.findAllByBlockingMember(blockingMember);
    }

    private void matchBetween(final MatchingResult matchingResult) {
        matchingResultRepository.save(matchingResult);
        matchingResult.matchEach();
    }
}
