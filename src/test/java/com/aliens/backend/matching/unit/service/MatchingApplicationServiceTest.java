package com.aliens.backend.matching.unit.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.business.model.Language;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingApplicationResponse;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.MatchingProcessService;
import com.aliens.backend.member.domain.MatchingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MatchingApplicationServiceTest extends BaseIntegrationTest {
    @Autowired MatchingApplicationService matchingApplicationService;
    @Autowired MatchingApplicationRepository matchingApplicationRepository;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MatchingProcessService matchingProcessService;
    @Autowired MatchingResultRepository matchingResultRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired DummyGenerator dummyGenerator;
    @Autowired MockClock mockClock;

    Member member;
    LoginMember loginMember;
    MatchingApplicationRequest matchingApplicationRequest = new MatchingApplicationRequest(Language.KOREAN, Language.ENGLISH);

    @BeforeEach
    void setUp() {
        member = dummyGenerator.generateSingleMember();
        loginMember = member.getLoginMember();

        dummyGenerator.generateMatchingRound(MockTime.TUESDAY);
        mockClock.mockTime(MockTime.VALID_RECEPTION_TIME_ON_TUESDAY);
    }

    @Test
    @DisplayName("매칭 신청 단위 테스트")
    void applyMatchTest() {
        // given
        Long expectedResult = loginMember.memberId();

        // when
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);

        // then
        MatchingApplication result = findMatchingApplication(loginMember);
        assertThat(result.getMemberId()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("매칭 신청 정보 수정 테스트")
    void modifyMatchingApplicationTest() {
        // given
        dummyGenerator.applySingleMemberToMatch(member, matchingApplicationRequest);
        MatchingApplicationRequest modifyRequest = new MatchingApplicationRequest(Language.JAPANESE, Language.ENGLISH);

        // when
        matchingApplicationService.modifyMatchingApplication(loginMember, modifyRequest);

        // then
        MatchingApplication result = findMatchingApplication(loginMember);
        assertThat(result.getFirstPreferLanguage()).isEqualTo(modifyRequest.firstPreferLanguage());
    }

    @Test
    @DisplayName("매칭된 회원들이 다음 매칭 신청")
    void applyNextMatch() {
        // given
        List<Member> members = dummyGenerator.generateMultiMember(10);
        Member memberWishingToApplyAgain = members.get(0);

        dummyGenerator.generateAppliersToMatch(members);
        dummyGenerator.operateMatching();

        dummyGenerator.generateMatchingRound(MockTime.FRIDAY);
        mockClock.mockTime(MockTime.VALID_RECEPTION_TIME_ON_FRIDAY);

        // when
        dummyGenerator.applySingleMemberToMatch(memberWishingToApplyAgain, matchingApplicationRequest);

        // then
        Member appliedMember = getMemberById(memberWishingToApplyAgain.getId());
        String result = appliedMember.getStatus();
        assertThat(result).isEqualTo(MatchingStatus.APPLIED_MATCHED.getMessage());
    }

    @Test
    @DisplayName("매칭된 회원이 다음 매칭 신청 후, 매칭 취소")
    void applyNextMatchAndCancel() {
        // given
        List<Member> members = dummyGenerator.generateMultiMember(10);
        Member memberWishingToCancel = members.get(0);

        dummyGenerator.generateAppliersToMatch(members);
        dummyGenerator.operateMatching();

        dummyGenerator.generateMatchingRound(MockTime.FRIDAY);
        mockClock.mockTime(MockTime.VALID_RECEPTION_TIME_ON_FRIDAY);
        dummyGenerator.applySingleMemberToMatch(memberWishingToCancel, matchingApplicationRequest);

        // when
        matchingApplicationService.cancelMatchingApplication(memberWishingToCancel.getLoginMember());

        // then
        Member canceledMember = getMemberById(memberWishingToCancel.getId());
        String result = canceledMember.getStatus();
        assertThat(result).isEqualTo(MatchingStatus.NOT_APPLIED_MATCHED.getMessage());
    }

    @Test
    @DisplayName("지정 시간 외 매칭 신청시, 에러 발생")
    void applyMatchIfNotValidTime() {
        // given
        mockClock.mockTime(MockTime.INVALID_RECEPTION_TIME);

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest))
                .hasMessage(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME.getDevelopCode());
    }

    @Test
    @DisplayName("매칭 신청 조회 단위 테스트")
    void getMatchingApplicationTest() {
        // given
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);

        //when
        MatchingApplicationResponse matchingApplicationResponse = matchingApplicationService.findMatchingApplication(loginMember);

        // then
        Long result = matchingApplicationResponse.memberId();
        assertThat(result).isEqualTo(loginMember.memberId());
    }

    @Test
    @DisplayName("매칭 신청하지 않은 사용자 조회 테스트")
    void getMatchingApplicationIfNotApplied() {
        assertThatThrownBy(() -> matchingApplicationService.findMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    @Test
    @DisplayName("매칭 신청 취소 단위 테스트")
    void deleteMatchingApplicationTest() {
        // given
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);

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
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);
        mockClock.mockTime(MockTime.INVALID_RECEPTION_TIME);

        // when & then
        assertThatThrownBy(() -> matchingApplicationService.cancelMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME.getDevelopCode());
    }

    @Test
    @DisplayName("매칭을 신청하지 않은 사용자 매칭 삭제 요청 테스트")
    void deleteMatchIfNotApplied() {
        assertThatThrownBy(() -> matchingApplicationService.cancelMatchingApplication(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
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
}
