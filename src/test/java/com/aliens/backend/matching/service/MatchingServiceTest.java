package com.aliens.backend.matching.service;

import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class MatchingServiceTest extends BaseIntegrationTest {

    @Autowired MatchingService matchingService;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;

    MatchingRound currentRound;

    @BeforeEach
    void setUp() {
        LocalDateTime roundBeginTime = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.of(roundBeginTime, matchingTimeProperties));
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    @Test
    @DisplayName("매칭을 신청한 적이 없는 회원이 매칭 조회")
    void getMatchingResultTest() {
        assertThatThrownBy(() -> matchingService.findMatchingResult(1L))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }
}
