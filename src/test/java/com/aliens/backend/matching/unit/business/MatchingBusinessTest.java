package com.aliens.backend.matching.unit.business;

import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.business.MatchingBusiness;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingProcessService;
import com.aliens.backend.mathcing.business.model.Participant;
import org.assertj.core.api.Assertions;
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

    @BeforeEach
    void setUp() {
        LocalDateTime roundBeginTime = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.from(roundBeginTime, matchingTimeProperties));
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    @Test
    @DisplayName("매칭 로직 실행 테스트")
    void matchingLogicTest() {
        mockClock.mockTime(MockTime.VALID_TIME);
        dummyGenerator.generateAppliersToMatch(15L);

        matchingBusiness.operateMatching(matchingApplicationRepository.findAllByMatchingRound(currentRound));
        List<Participant> result = matchingBusiness.getMatchedParticipants();

        result.forEach(participant -> assertThat(participant.partners()).isNotNull());
    }

    @Test
    @DisplayName("매칭 결과 조회")
    void operateMatchingTest() {
        // given
        mockClock.mockTime(MockTime.VALID_TIME);
        dummyGenerator.generateAppliersToMatch(20L);

        // when
        matchingProcessService.operateMatching();

        // then
        List<MatchingResult> result = matchingResultRepository.findAllByMatchingRound(currentRound);
        Assertions.assertThat(result).isNotNull();
    }
}
