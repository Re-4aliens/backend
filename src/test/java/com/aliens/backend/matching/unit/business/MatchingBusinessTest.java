package com.aliens.backend.matching.unit.business;

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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MatchingBusinessTest extends BaseServiceTest {
    @Autowired
    MatchingProcessService matchingProcessService;
    @Autowired MatchingApplicationRepository matchingApplicationRepository;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingResultRepository matchingResultRepository;
    @Autowired MatchingBusiness matchingBusiness;
    @Autowired DummyGenerator dummyGenerator;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MockClock mockClock;

    MatchingRound currentRound;
    MatchingOperateRequest matchingOperateRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime roundBeginTime = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.from(roundBeginTime, matchingTimeProperties));
        currentRound = getCurrentRound();
    }

    @Test
    @DisplayName("매칭 로직 실행 테스트")
    void matchingLogicTest() {
        mockClock.mockTime(MockTime.VALID_TIME);
        dummyGenerator.generateAppliersToMatch(20L);
        matchingOperateRequest = createOperateRequest(currentRound);

        matchingBusiness.operateMatching(matchingOperateRequest);

        List<Participant> result = matchingBusiness.getMatchedParticipants();
        result.forEach(participant -> assertThat(participant.partners()).isNotNull());
    }

    @Test
    @DisplayName("직전 회차에 매칭된 사용자와 매칭되지 않는 기능 테스트")
    void isDuplicateMatchingTest() {
        mockClock.mockTime(MockTime.VALID_TIME);
        dummyGenerator.generateAppliersToMatch(20L);
        matchingOperateRequest = createOperateRequest(currentRound);

        // when
        matchingBusiness.operateMatching(matchingOperateRequest);

        // then

    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private List<MatchingApplication> getMatchingApplications(MatchingRound matchingRound) {
        return matchingApplicationRepository.findAllByMatchingRound(matchingRound);
    }

    private MatchingRound getPreviousMatchingRound(MatchingRound matchingRound) {
        Long previousRound = matchingRound.getRound() - 1;
        return matchingRoundRepository.findMatchingRoundByRound(previousRound)
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private List<MatchingResult> getPreviousMatchingResult(MatchingRound matchingRound) {
        MatchingRound previousMatchingRound = getPreviousMatchingRound(matchingRound);
        return matchingResultRepository.findAllByMatchingRound(previousMatchingRound);
    }

    private MatchingOperateRequest createOperateRequest(MatchingRound matchingRound) {
        List<MatchingApplication> matchingApplications = getMatchingApplications(matchingRound);
        List<MatchingResult> previousMatchingResult = getPreviousMatchingResult(matchingRound);
        return MatchingOperateRequest.of(matchingApplications, previousMatchingResult);
    }
}
