package com.aliens.backend.matching.unit.service;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingRoundService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class MatchingRoundServiceTest extends BaseIntegrationTest {
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MatchingRoundService matchingRoundService;
    @Autowired DummyGenerator dummyGenerator;
    @Autowired MockClock mockClock;

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

    @Test
    @DisplayName("매칭 회차가 업데이트 된 00시에 매칭된 회원들에게 알림이 발송됨")
    void sendMatchedNotification() {
        // given
        List<Member> members = dummyGenerator.generateMultiMember(5);
        dummyGenerator.generateMatchingRound(MockTime.VALID_RECEPTION_TIME_ON_TUESDAY);
        dummyGenerator.generateAppliersToMatch(members);
        dummyGenerator.operateMatching();

        // when
        mockClock.mockTime(MockTime.FRIDAY);
        matchingRoundService.saveMatchRound();

        // then
        verify(fcmSender, times(1)).sendMatchedNotification(any());
    }
}
