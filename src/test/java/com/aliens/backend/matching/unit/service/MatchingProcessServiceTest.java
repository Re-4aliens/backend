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
import com.aliens.backend.mathcing.service.MatchingProcessService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MatchingProcessServiceTest extends BaseServiceTest {
    @Autowired MatchingProcessService matchingProcessService;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MatchingResultRepository matchingResultRepository;
    @Autowired MockClock mockClock;
    @Autowired DummyGenerator dummyGenerator;

    LoginMember loginMember;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember(1L, MemberRole.MEMBER);
        saveMatchRound(MockTime.MONDAY);
    }

    @Test
    @DisplayName("매칭 결과 조회")
    void operateMatchingTest() {
        // given
        mockClock.mockTime(MockTime.VALID_RECEPTION_TIME_ON_MONDAY);
        dummyGenerator.generateAppliersToMatch(20L);

        // when
        matchingProcessService.operateMatching();

        // then
        List<MatchingResult> result = matchingResultRepository.findAllByMatchingRound(getCurrentRound());
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("연속 매칭 테스트")
    void operateMatchingTwice() {
        operateMatching(MockTime.VALID_RECEPTION_TIME_ON_MONDAY);
        saveMatchRound(MockTime.THURSDAY);
        operateMatching(MockTime.VALID_RECEPTION_TIME_ON_THURSDAY);
    }

    @Test
    @DisplayName("직전 회차에 매칭된 사용자와 매칭되지 않는 기능 테스트")
    void isDuplicateMatchingTest() {
        // given & when
        operateMatching(MockTime.VALID_RECEPTION_TIME_ON_MONDAY);
        List<MatchingResult> firstRoundResult = getMatchingResultByMatchingRound(getCurrentRound());
        saveMatchRound(MockTime.THURSDAY);
        operateMatching(MockTime.VALID_RECEPTION_TIME_ON_THURSDAY);
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

    }

    @Test
    @DisplayName("매칭을 신청한 적이 없는 회원이 매칭 조회")
    void getMatchingResultTest() {
        assertThatThrownBy(() -> matchingProcessService.findMatchingResult(loginMember))
                .hasMessage(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO.getDevelopCode());
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private void saveMatchRound(MockTime mockTime) {
        mockClock.mockTime(mockTime);
        MatchingRound matchingRound = MatchingRound.from(mockTime.getTime(), matchingTimeProperties);
        matchingRoundRepository.save(matchingRound);
    }

    private void operateMatching(MockTime mockTime) {
        mockClock.mockTime(mockTime);
        dummyGenerator.generateAppliersToMatch(20L);
        matchingProcessService.operateMatching();
    }

    private List<MatchingResult> getMatchingResultByMatchingRound(MatchingRound matchingRound) {
        return matchingResultRepository.findAllByMatchingRound(matchingRound);
    }
}
