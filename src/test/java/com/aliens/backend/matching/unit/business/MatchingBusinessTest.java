package com.aliens.backend.matching.unit.business;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.matching.util.MockClock;
import com.aliens.backend.matching.util.MockTime;
import com.aliens.backend.mathcing.business.MatchingBusiness;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MatchingBusinessTest {
    @Autowired
    MatchingApplicationService matchingApplicationService;

    @Autowired
    MatchingApplicationRepository matchingApplicationRepository;

    @Autowired
    MatchingRoundRepository matchingRoundRepository;

    @Autowired
    MatchingBusiness matchingBusiness;

    @Autowired
    MatchingApplicationGenerator matchingApplicationGenerator;

    @Autowired
    MatchingTimeProperties matchingTimeProperties;

    @Autowired
    MockClock mockClock;

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
    void matchingLogicTest() {
        mockClock.mockTime(currentRound, MockTime.VALID_TIME);
        matchingApplicationGenerator.applyToMatch(15L);

        matchingBusiness.operateMatching();

        matchingBusiness.getMatchedParticipants().forEach(participant -> assertThat(participant.partners()).isNotNull());
    }
}
