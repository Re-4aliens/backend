package com.aliens.backend.matching.unit.service;

import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MatchingRoundServiceTest extends BaseServiceTest {
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;

    @Test
    @DisplayName("매주 월, 목 매칭 회차 업데이트")
    void saveMatchRoundTest() {
        // given
        MatchingRound mondayRound = MatchingRound.from(MockTime.TUESDAY.getTime(), matchingTimeProperties);

        // when
        matchingRoundRepository.save(mondayRound);

        // then
        MatchingRound currentRound = getCurrentRound();
        DayOfWeek result = currentRound.getDayOfWeek();
        DayOfWeek expected = DayOfWeek.TUESDAY;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("현재 매칭 회차 조회")
    void getCurrentRoundTest() {
        // given
        MatchingRound mondayRound = MatchingRound.from(MockTime.TUESDAY.getTime(), matchingTimeProperties);
        matchingRoundRepository.save(mondayRound);

        // then
        MatchingRound currentRound = getCurrentRound();
        DayOfWeek result = currentRound.getDayOfWeek();
        DayOfWeek expected = DayOfWeek.TUESDAY;
        assertThat(result).isEqualTo(expected);
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }
}
