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

    final LocalDateTime VALID_TIME = LocalDateTime.of(2024, 1, 29, 10, 0);
    final LocalDateTime INVALID_TIME = LocalDateTime.of(2024, 1, 29, 19, 0);

    @BeforeEach
    void setUp() {
        LocalDateTime roundBeginTime = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.of(roundBeginTime, matchingTimeProperties));
        matchingApplicationRequest = new MatchingApplicationRequest(1L, Language.KOREAN, Language.ENGLISH);
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    @Test
    @DisplayName("매칭 신청 단위 테스트")
    @Transactional
    void applyMatchTest() {
        // given
        mockTime(VALID_TIME);

        // when
        matchingApplicationService.saveParticipant(matchingApplicationRequest);

        // then
        MatchingApplication result = matchingApplicationRepository.findById(MatchingApplicationId.of(currentRound, matchingApplicationRequest.memberId()))
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO));
        assertThat(result.getId().getMemberId()).isEqualTo(matchingApplicationRequest.memberId());
    }

    @Test
    @DisplayName("지정 시간 외 매칭 신청시, 에러 발생")
    @Transactional
    void applyMatchIfNotValidTime() {
        // given
        mockTime(INVALID_TIME);

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.saveParticipant(matchingApplicationRequest))
                .isInstanceOf(RestApiException.class)
                .hasMessage(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME.getMessage());
    }


    @Test
    @DisplayName("매칭 신청 조회 단위 테스트")
    @Transactional
    void getMatchingApplicationTest() {
        // given
        applyToMatch();

        //when
        MatchingApplicationResponse result = matchingApplicationService
                .findMatchingApplication(matchingApplicationRequest.memberId());

        // then
        assertThat(result.memberId()).isEqualTo(matchingApplicationRequest.memberId());
    }

    @Test
    @DisplayName("매칭 신청하지 않은 사용자 조회 테스트")
    @Transactional
    void getMatchingApplicationIfNotApplied() {
        // when & then
        assertThatThrownBy(() -> matchingApplicationService.findMatchingApplication(matchingApplicationRequest.memberId()))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getMessage());
    }

    @Test
    @DisplayName("매칭 신청 취소 단위 테스트")
    @Transactional
    void deleteMatchingApplicationTest() {
        // given
        applyToMatch();
        mockTime(VALID_TIME);

        // when
        matchingApplicationService.deleteMatchingApplication(matchingApplicationRequest.memberId());

        // then
        assertThatThrownBy(() -> matchingApplicationService.findMatchingApplication(matchingApplicationRequest.memberId()))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getMessage());
    }

    @Test
    @DisplayName("지정 시간 외 매칭 취소 신청시, 에러 발생")
    @Transactional
    void deleteMatchIfNotValidTime() {
        // given
        applyToMatch();
        mockTime(INVALID_TIME);

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.deleteMatchingApplication(matchingApplicationRequest.memberId()))
                .hasMessage(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME.getMessage());
    }

    @Test
    @DisplayName("매칭을 신청하지 않은 사용자 매칭 삭제 요청 테스트")
    @Transactional
    void deleteMatchIfNotApplied() {
        // given
        mockTime(VALID_TIME);

        assertThatThrownBy(() -> matchingApplicationService.deleteMatchingApplication(matchingApplicationRequest.memberId()))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getMessage());
    }

    private void applyToMatch() {
        mockTime(VALID_TIME);
        matchingApplicationService.saveParticipant(matchingApplicationRequest);
    }

    private void mockTime(LocalDateTime time) {
        Clock fixedClock = Clock.fixed(time.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        doCallRealMethod().when(matchingApplicationValidator).checkReceptionTime(currentRound, time);
    }
}
