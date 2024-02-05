package com.aliens.backend.matching.unit.business;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.matching.util.MockClock;
import com.aliens.backend.matching.util.MockTime;
import com.aliens.backend.mathcing.business.MatchingBusiness;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.MatchingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MatchingBusinessTest {
    @Autowired MatchingService matchingService;
    @Autowired MatchingApplicationRepository matchingApplicationRepository;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingResultRepository matchingResultRepository;
    @Autowired MatchingBusiness matchingBusiness;
    @Autowired MatchingApplicationGenerator matchingApplicationGenerator;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MockClock mockClock;

    MatchingRound currentRound;

    @BeforeEach
    void setUp() {
        LocalDateTime roundBeginTime = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.of(roundBeginTime, matchingTimeProperties));
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    @Test
    @Transactional
    @DisplayName("매칭 로직 실행 테스트")
    void matchingLogicTest() {
        mockClock.mockTime(currentRound, MockTime.VALID_TIME);
        matchingApplicationGenerator.applyToMatch(15L);

        matchingBusiness.operateMatching(matchingApplicationRepository.findAllByMatchingRound(currentRound));

        matchingBusiness.getMatchedParticipants().forEach(participant -> assertThat(participant.partners()).isNotNull());
    }

    @Test
    @DisplayName("매칭 결과 조회")
    @Transactional
    void operateMatchingTest() {
        // given
        mockClock.mockTime(currentRound, MockTime.VALID_TIME);
        matchingApplicationGenerator.applyToMatch(20L);

        // when
        matchingService.operateMatching();

        // then
        List<MatchingResult> result = matchingResultRepository.findAllByMatchingRound(currentRound);
        Assertions.assertThat(result).isNotNull();
    }
}
