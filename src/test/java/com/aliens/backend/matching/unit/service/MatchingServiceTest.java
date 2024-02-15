package com.aliens.backend.matching.unit.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.MemberRole;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MatchingServiceTest extends BaseServiceTest {

    @Autowired MatchingService matchingService;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MatchingResultRepository matchingResultRepository;
    @Autowired MockClock mockClock;
    @Autowired DummyGenerator dummyGenerator;

    MatchingRound currentRound;
    LoginMember loginMember;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember(1L, MemberRole.MEMBER);
        LocalDateTime roundBeginTime = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.from(roundBeginTime, matchingTimeProperties));
        currentRound = getCurrentRound();
    }

    @Test
    @DisplayName("매칭을 신청한 적이 없는 회원이 매칭 조회")
    void getMatchingResultTest() {
        assertThatThrownBy(() -> matchingService.findMatchingResult(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    @Test
    @DisplayName("매칭 결과 조회")
    void operateMatchingTest() {
        // given
        mockClock.mockTime(MockTime.VALID_TIME);
        dummyGenerator.generateAppliersToMatch(20L);

        // when
        matchingService.operateMatching();

        // then
        List<MatchingResult> result = matchingResultRepository.findAllByMatchingRound(currentRound);
        Assertions.assertThat(result).isNotNull();
    }
}
