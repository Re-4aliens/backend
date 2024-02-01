package com.aliens.backend.matching.unit.service;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.id.MatchingApplicationId;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.model.Language;
import com.aliens.backend.mathcing.validator.MatchingApplicationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.aliens.backend.mathcing.controller.dto.request.MatchingRequest.*;
import static com.aliens.backend.mathcing.controller.dto.response.MatchingResponse.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@SpringBootTest
public class MatchingApplicationServiceTest {
    @Autowired
    MatchingApplicationService matchingApplicationService;

    @Autowired
    MatchingApplicationRepository matchingApplicationRepository;

    @Autowired
    MatchingRoundRepository matchingRoundRepository;

    @Autowired
    MatchingTimeProperties matchingTimeProperties;

    @MockBean
    MatchingApplicationValidator matchingApplicationValidator;

    @MockBean
    Clock clock;

    MatchingApplicationRequest matchingApplicationRequest;
    MatchingRound currentRound;

    @BeforeEach
    void setUp() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 29, 0, 0);

        matchingRoundRepository.save(MatchingRound.of(testTime, matchingTimeProperties));

        matchingApplicationRequest = new MatchingApplicationRequest(1L, Language.KOREAN, Language.ENGLISH);
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    @Test
    @DisplayName("매칭 신청 단위 테스트")
    @Transactional
    void applyMatchTest() {
        // given
        LocalDateTime validTime = LocalDateTime.of(2024, 1, 29, 10, 0);
        mockTime(validTime);
        given(matchingApplicationValidator.canApplyMatch(currentRound, validTime)).willCallRealMethod();

        // when
        matchingApplicationService.saveParticipant(matchingApplicationRequest);

        // then
        MatchingApplication result = matchingApplicationRepository.findById(MatchingApplicationId.of(currentRound, matchingApplicationRequest.memberId()))
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO));
        assertThat(result.getMatchingApplicationId().getMemberId()).isEqualTo(matchingApplicationRequest.memberId());
    }

    @Test
    @DisplayName("지정 시간 외 매칭 신청시, 에러 발생")
    @Transactional
    void applyMatchIfNotValidTime() {
        // given
        LocalDateTime invalidTime = LocalDateTime.of(2024, 1, 29, 19, 0);
        mockTime(invalidTime);
        given(matchingApplicationValidator.canApplyMatch(currentRound, invalidTime)).willCallRealMethod();

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.saveParticipant(matchingApplicationRequest))
                .isInstanceOf(RestApiException.class)
                .hasMessage(MatchingError.NOT_VALID_MATCHING_TIME.getMessage());
    }


    @Test
    @DisplayName("매칭 신청 조회 단위 테스트")
    @Transactional
    void getMatchingApplicationTest() {
        applyToMatch();

        MatchingApplicationResponse result = matchingApplicationService
                .findMatchingApplication(matchingApplicationRequest.memberId());

        assertThat(result.memberId()).isEqualTo(matchingApplicationRequest.memberId());
    }

    private void applyToMatch() {
        LocalDateTime validTime = LocalDateTime.of(2024, 1, 29, 10, 0);
        mockTime(validTime);
        given(matchingApplicationValidator.canApplyMatch(currentRound, validTime)).willReturn(true);
        matchingApplicationService.saveParticipant(matchingApplicationRequest);
    }

    private void mockTime(LocalDateTime time) {
        Clock fixedClock = Clock.fixed(time.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
    }
}
