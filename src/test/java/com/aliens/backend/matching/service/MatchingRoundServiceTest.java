package com.aliens.backend.matching.service;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MatchingRoundServiceTest {
    @Autowired
    MatchingRoundRepository matchingRoundRepository;

    @Autowired
    MatchingTimeProperties matchingTimeProperties;

    @Test
    @DisplayName("매주 월, 목 매칭 회차 업데이트")
    @Transactional
    void saveMatchRoundTest() {
        MatchingRound result = saveMatchRound();

        assertThat(result.getRound()).isNotNull();
    }

    @Test
    @DisplayName("현재 매칭 회차 조회")
    @Transactional
    void getCurrentRound() {
        saveMatchRound();

        MatchingRound result = matchingRoundRepository.findCurrentRound()
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));

        assertThat(result.getRound()).isNotNull();
    }

    private MatchingRound saveMatchRound() {
        LocalDateTime monday = LocalDateTime.of(2024, 1, 29, 0, 0);
        return matchingRoundRepository.save(MatchingRound.of(monday, matchingTimeProperties));
    }
}
