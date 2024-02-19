package com.aliens.backend.matching.unit.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingApplicationResponse;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.business.model.Language;
import com.aliens.backend.mathcing.service.MatchingProcessService;
import com.aliens.backend.member.domain.MatchingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.aliens.backend.matching.util.time.MockTime.*;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class MatchingApplicationServiceTest extends BaseServiceTest {
    @Autowired MatchingApplicationService matchingApplicationService;
    @Autowired MatchingApplicationRepository matchingApplicationRepository;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MatchingProcessService matchingProcessService;
    @Autowired MatchingResultRepository matchingResultRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired DummyGenerator dummyGenerator;
    @Autowired MockClock mockClock;

    LoginMember loginMember;
    MatchingApplicationRequest matchingApplicationRequest;

    @BeforeEach
    void setUp() {
        saveMatchRound(MockTime.TUESDAY);
        matchingApplicationRequest = new MatchingApplicationRequest(Language.KOREAN, Language.ENGLISH);
    }

    @Test
    @DisplayName("매칭 신청 단위 테스트")
    void applyMatchTest() {
        // given
        createMember();
        mockClock.mockTime(VALID_RECEPTION_TIME_ON_TUESDAY);
        Long expectedResult = loginMember.memberId();

        // when
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);

        // then
        MatchingApplication result = findMatchingApplication(loginMember);
        assertThat(result.getMemberId()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("매칭된 회원들이 다음 매칭 신청")
    void applyNextMatch() {
        // given
        dummyGenerator.generateMultiMember(20);
        operateMatching(VALID_RECEPTION_TIME_ON_TUESDAY);
        saveMatchRound(FRIDAY);
        mockClock.mockTime(VALID_RECEPTION_TIME_ON_FRIDAY);

        // when
        dummyGenerator.generateAppliersToMatch(20L);

        // then
        List<Member> members = getAllMembers();
        members.forEach(member -> {
            String result = member.getStatus();
            assertThat(result).isEqualTo(MatchingStatus.APPLIED_MATCHED.getMessage());
        });
    }

    @Test
    @DisplayName("매칭된 회원이 다음 매칭 신청 후, 매칭 취소")
    void applyNextMatchAndCancel() {
        // given
        dummyGenerator.generateMultiMember(20);
        operateMatching(VALID_RECEPTION_TIME_ON_TUESDAY);
        saveMatchRound(FRIDAY);
        mockClock.mockTime(VALID_RECEPTION_TIME_ON_FRIDAY);
        dummyGenerator.generateAppliersToMatch(20L);

        // when
        Member member = getMemberById(1L);
        matchingApplicationService.cancelMatchingApplication(member.getLoginMember());

        // then
        Member resultMember = getMemberById(1L);
        String result = resultMember.getStatus();
        String expected = MatchingStatus.NOT_APPLIED_MATCHED.getMessage();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("지정 시간 외 매칭 신청시, 에러 발생")
    void applyMatchIfNotValidTime() {
        // given
        createMember();
        mockClock.mockTime(INVALID_RECEPTION_TIME);

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest))
                .hasMessage(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME.getDevelopCode());
    }

    @Test
    @DisplayName("매칭 신청 조회 단위 테스트")
    void getMatchingApplicationTest() {
        // given
        createMember();
        Long expectedResult = loginMember.memberId();
        applyToMatch();

        //when
        MatchingApplicationResponse result = matchingApplicationService.findMatchingApplication(loginMember);

        // then
        assertThat(result.memberId()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("매칭 신청하지 않은 사용자 조회 테스트")
    void getMatchingApplicationIfNotApplied() {
        createMember();
        // when & then
        assertThatThrownBy(() -> matchingApplicationService.findMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    @Test
    @DisplayName("매칭 신청 취소 단위 테스트")
    void deleteMatchingApplicationTest() {
        // given
        createMember();
        applyToMatch();
        mockClock.mockTime(VALID_RECEPTION_TIME_ON_TUESDAY);

        // when
        matchingApplicationService.cancelMatchingApplication(loginMember);

        // then
        assertThatThrownBy(() -> matchingApplicationService.findMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    @Test
    @DisplayName("지정 시간 외 매칭 취소 신청시, 에러 발생")
    void deleteMatchIfNotValidTime() {
        // given
        createMember();
        applyToMatch();
        mockClock.mockTime(INVALID_RECEPTION_TIME);

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.cancelMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME.getDevelopCode());
    }

    @Test
    @DisplayName("매칭을 신청하지 않은 사용자 매칭 삭제 요청 테스트")
    void deleteMatchIfNotApplied() {
        // given
        createMember();
        mockClock.mockTime(VALID_RECEPTION_TIME_ON_TUESDAY);

        assertThatThrownBy(() -> matchingApplicationService.cancelMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    private void applyToMatch() {
        mockClock.mockTime(VALID_RECEPTION_TIME_ON_TUESDAY);
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);
    }

    private void saveMatchRound(MockTime mockTime) {
        mockClock.mockTime(mockTime);
        MatchingRound matchingRound = MatchingRound.from(mockTime.getTime(), matchingTimeProperties);
        matchingRoundRepository.save(matchingRound);
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private MatchingApplication findMatchingApplication(LoginMember loginMember) {
        return matchingApplicationRepository.findByMatchingRoundAndMemberId(getCurrentRound(), loginMember.memberId())
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    private void createMember() {
        Member member = dummyGenerator.generateSingleMember();
        loginMember = member.getLoginMember();
    }

    private void operateMatching(MockTime mockTime) {
        mockClock.mockTime(mockTime);
        dummyGenerator.generateAppliersToMatch(20L);
        matchingProcessService.operateMatching();
    }
}
