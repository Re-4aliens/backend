package com.aliens.backend.matching.unit.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.block.domain.Block;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingResultRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingProcessService;
import com.aliens.backend.member.domain.MatchingStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class MatchingProcessServiceTest extends BaseIntegrationTest {
    @Autowired MatchingProcessService matchingProcessService;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MatchingResultRepository matchingResultRepository;
    @Autowired BlockRepository blockRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MockClock mockClock;
    @Autowired DummyGenerator dummyGenerator;

    List<Member> members;

    @BeforeEach
    void setUp() {
        members = dummyGenerator.generateMultiMember(10);
        dummyGenerator.generateMatchingRound(MockTime.TUESDAY);
        mockClock.mockTime(MockTime.VALID_RECEPTION_TIME_ON_TUESDAY);
        dummyGenerator.generateAppliersToMatch(members);
    }

    @Test
    @DisplayName("매칭 결과 조회")
    void operateMatchingTest() {
        // given & when
        dummyGenerator.operateMatching();

        // then
        List<MatchingResult> result = getMatchingResultByMatchingRound(getCurrentRound());
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("매칭 결과 기반으로 채팅방 개설 이벤트 발송")
    void createChatRoomEventTest() {
        // given & when
        dummyGenerator.operateMatching();

        // then
        verify(chatService, times(1)).handleChatRoomCreationEvent(any());
    }

    @Test
    @DisplayName("매칭 완료 후, 알림이 전송되는지 테스트")
    void sendNotificationTest() {
        // given & when
        dummyGenerator.operateMatching();

        // then
        verify(fcmSender, times(1)).listenMultiMessageRequest(any());
    }

    @Test
    @DisplayName("연속 매칭 테스트")
    void operateMatchingTwice() {
        dummyGenerator.operateMatching();

        dummyGenerator.generateMatchingRound(MockTime.FRIDAY);
        mockClock.mockTime(MockTime.VALID_RECEPTION_TIME_ON_FRIDAY);
        dummyGenerator.generateAppliersToMatch(members);
        dummyGenerator.operateMatching();

        List<MatchingResult> result = getMatchingResultByMatchingRound(getCurrentRound());
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("직전 회차에 매칭된 사용자와 매칭되지 않는 기능 테스트")
    void isDuplicateMatchingTest() {
        // given & when
        dummyGenerator.operateMatching();
        List<MatchingResult> firstRoundResult = getMatchingResultByMatchingRound(getCurrentRound());

        dummyGenerator.generateMatchingRound(MockTime.FRIDAY);
        mockClock.mockTime(MockTime.VALID_RECEPTION_TIME_ON_FRIDAY);
        dummyGenerator.generateAppliersToMatch(members);
        dummyGenerator.operateMatching();
        List<MatchingResult> secondRoundResult = getMatchingResultByMatchingRound(getCurrentRound());

        Map<Long, Set<Long>> matchedMembersInSecondRound = secondRoundResult.stream()
                .collect(Collectors.groupingBy(
                        MatchingResult::getMatchingMemberId,
                        Collectors.mapping(MatchingResult::getMatchedMemberId, Collectors.toSet())
                ));

        // then
        firstRoundResult.forEach(first -> {
            Set<Long> matchedMemberIds = matchedMembersInSecondRound.get(first.getMatchingMemberId());
            assertThat(matchedMemberIds).doesNotContain(first.getMatchedMemberId());
        });
    }

    @Test
    @DisplayName("차단된 유저와 매칭이 되지 않는지 테스트")
    void isBlockedMemberTest() {
        // given
        Member blockingMember = members.get(0);
        makeThisMemberBlockAllPartner(blockingMember);

        // when
        dummyGenerator.operateMatching();

        // then
        List<MatchingResult> matchingResults = getMatchingResultByMatchingRound(getCurrentRound());
        List<MatchingResult> matchingResultsOfBlockingMember = matchingResults.stream()
                .filter(matchingResult -> matchingResult.getMatchingMemberId().equals(blockingMember.getId())).toList();
        assertThat(matchingResultsOfBlockingMember).isEmpty();
    }

    @Test
    @DisplayName("매칭 만료시, 사용자 상태가 변경되는지 테스트")
    void resetPreviousMatching() {
        // given
        dummyGenerator.operateMatching();
        dummyGenerator.generateMatchingRound(MockTime.FRIDAY);

        // when
        matchingProcessService.expireMatching();

        // then
        Member expiredMember = getMemberById(members.get(0).getId());

        String result = expiredMember.getStatus();
        assertThat(result).isEqualTo(MatchingStatus.NOT_APPLIED_NOT_MATCHED.getMessage());
    }

    @Test
    @DisplayName("매칭을 신청한 적이 없는 회원이 매칭 결과 조회")
    void getMatchingResultTest() {
        Member notAppliedMember = dummyGenerator.generateSingleMember();
        LoginMember loginMember = notAppliedMember.getLoginMember();

        assertThatThrownBy(() -> matchingProcessService.findMatchingResult(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private List<MatchingResult> getMatchingResultByMatchingRound(MatchingRound matchingRound) {
        Long round = matchingRound.getRound();
        return matchingResultRepository.findAllByRound(round);
    }

    private void makeThisMemberBlockAllPartner(Member blockingMember) {
        for (int i = 1; i < members.size(); i++) {
            Member blockedMember = members.get(i);
            Block blockRequest = Block.of(blockedMember, blockingMember);
            blockRepository.save(blockRequest);
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }
}
