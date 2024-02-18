package com.aliens.backend.matching.unit.business;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.block.domain.Block;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.business.MatchingBusiness;
import com.aliens.backend.mathcing.controller.dto.request.MatchingOperateRequest;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingProcessService;
import com.aliens.backend.mathcing.business.model.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MatchingBusinessTest extends BaseServiceTest {
    @Autowired MatchingProcessService matchingProcessService;
    @Autowired MatchingApplicationRepository matchingApplicationRepository;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingResultRepository matchingResultRepository;
    @Autowired MatchingBusiness matchingBusiness;
    @Autowired DummyGenerator dummyGenerator;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired BlockRepository blockRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MockClock mockClock;

    MatchingOperateRequest matchingOperateRequest;

    @BeforeEach
    void setUp() {
        saveMatchRound(MockTime.MONDAY);
    }

    @Test
    @DisplayName("매칭 로직 실행 테스트")
    void matchingLogicTest() {
        operateMatching(MockTime.VALID_RECEPTION_TIME_ON_MONDAY);

        List<Participant> result = matchingBusiness.getMatchedParticipants();
        result.forEach(participant -> assertThat(participant.partners()).isNotNull());
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private List<MatchingApplication> getMatchingApplications(MatchingRound matchingRound) {
        return matchingApplicationRepository.findAllByMatchingRound(matchingRound);
    }

    private List<MatchingResult> getPreviousMatchingResults() {
        MatchingRound currentRound = getCurrentRound();
        Long previousRound = currentRound.getPreviousRound();
        return matchingResultRepository.findAllByRound(previousRound);
    }

    private List<Block> getBlockListByMatchingApplications(List<MatchingApplication> matchingApplications) {
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
        List<MatchingResult> previousMatchingResult = getPreviousMatchingResults();
        List<Block> participantBlockHistory = getBlockListByMatchingApplications(matchingApplications);
        return MatchingOperateRequest.of(matchingApplications, previousMatchingResult, participantBlockHistory);
    }

    private void saveMatchRound(MockTime mockTime) {
        mockClock.mockTime(mockTime);
        MatchingRound matchingRound = MatchingRound.from(mockTime.getTime(), matchingTimeProperties);
        matchingRoundRepository.save(matchingRound);
    }

    private void operateMatching(MockTime mockTime) {
        mockClock.mockTime(mockTime);
        dummyGenerator.generateMultiMember(21);
        dummyGenerator.generateAppliersToMatch(20L);
        matchingOperateRequest = createOperateRequest(getCurrentRound());
        matchingBusiness.operateMatching(matchingOperateRequest);
    }
}
