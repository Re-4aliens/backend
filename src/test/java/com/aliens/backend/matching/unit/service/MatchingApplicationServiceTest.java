package com.aliens.backend.matching.unit.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.MemberRole;
import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.matching.util.MockClock;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingApplicationResponse;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.id.MatchingApplicationId;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.model.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.aliens.backend.matching.util.MockTime.INVALID_TIME;
import static com.aliens.backend.matching.util.MockTime.VALID_TIME;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class MatchingApplicationServiceTest {
    @Autowired MatchingApplicationService matchingApplicationService;
    @Autowired MatchingApplicationRepository matchingApplicationRepository;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MockClock mockClock;

    LoginMember loginMember;
    MatchingApplicationRequest matchingApplicationRequest;
    MatchingRound currentRound;

    @BeforeEach
    void setUp() {
        createNewMatchingRound();
        matchingApplicationRequest = new MatchingApplicationRequest(Language.KOREAN, Language.ENGLISH);
        loginMember = new LoginMember(1L, MemberRole.MEMBER);
        currentRound = getCurrentRound();
    }

    @Test
    @DisplayName("매칭 신청 단위 테스트")
    @Transactional
    void applyMatchTest() {
        // given
        mockClock.mockTime(VALID_TIME);
        Long expectedResult = loginMember.memberId();

        // when
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);

        // then
        MatchingApplication result = findMatchingApplication(loginMember);
        assertThat(result.getMemberId()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("지정 시간 외 매칭 신청시, 에러 발생")
    @Transactional
    void applyMatchIfNotValidTime() {
        // given
        mockClock.mockTime(INVALID_TIME);

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest))
                .hasMessage(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME.getDevelopCode());
    }


    @Test
    @DisplayName("매칭 신청 조회 단위 테스트")
    @Transactional
    void getMatchingApplicationTest() {
        // given
        applyToMatch();
        Long expectedResult = loginMember.memberId();

        //when
        MatchingApplicationResponse result = matchingApplicationService.findMatchingApplication(loginMember);

        // then
        assertThat(result.memberId()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("매칭 신청하지 않은 사용자 조회 테스트")
    @Transactional
    void getMatchingApplicationIfNotApplied() {
        // when & then
        assertThatThrownBy(() -> matchingApplicationService.findMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    @Test
    @DisplayName("매칭 신청 취소 단위 테스트")
    @Transactional
    void deleteMatchingApplicationTest() {
        // given
        applyToMatch();
        mockClock.mockTime(VALID_TIME);

        // when
        matchingApplicationService.deleteMatchingApplication(loginMember);

        // then
        assertThatThrownBy(() -> matchingApplicationService.findMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    @Test
    @DisplayName("지정 시간 외 매칭 취소 신청시, 에러 발생")
    @Transactional
    void deleteMatchIfNotValidTime() {
        // given
        applyToMatch();
        mockClock.mockTime(INVALID_TIME);

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.deleteMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME.getDevelopCode());
    }

    @Test
    @DisplayName("매칭을 신청하지 않은 사용자 매칭 삭제 요청 테스트")
    @Transactional
    void deleteMatchIfNotApplied() {
        // given
        mockClock.mockTime(VALID_TIME);

        assertThatThrownBy(() -> matchingApplicationService.deleteMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    private void applyToMatch() {
        mockClock.mockTime(VALID_TIME);
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);
    }

    private void createNewMatchingRound() {
        LocalDateTime roundBeginTime = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.of(roundBeginTime, matchingTimeProperties));
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private MatchingApplication findMatchingApplication(LoginMember loginMember) {
        return matchingApplicationRepository
                .findById(MatchingApplicationId.of(currentRound, loginMember.memberId()))
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO));
    }
}
