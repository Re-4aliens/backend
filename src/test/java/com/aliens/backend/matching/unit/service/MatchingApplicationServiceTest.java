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

import java.time.LocalDateTime;

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

    MatchingApplicationRequest matchingApplicationRequest;
    MatchingRound currentRound;

    @BeforeEach
    void setUp() {
        LocalDateTime monday = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.of(monday, matchingTimeProperties));

        matchingApplicationRequest = new MatchingApplicationRequest(1L, Language.KOREAN, Language.ENGLISH);
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    @Test
    @DisplayName("매칭 신청 단위 테스트")
    @Transactional
    void applyMatchTest() {
        given(matchingApplicationValidator.canApplyMatching(any())).willReturn(true);

        matchingApplicationService.saveParticipant(matchingApplicationRequest);

        MatchingApplication result =
                matchingApplicationRepository.findById(
                        MatchingApplicationId.of(currentRound, matchingApplicationRequest.memberId()))
                        .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO));
        assertThat(result.getMatchingApplicationId().getMemberId())
                .isEqualTo(matchingApplicationRequest.memberId());
    }

    @Test
    @DisplayName("매칭 신청 조회 테스트")
    @Transactional
    void getMatchingApplicationTest() {
        applyToMatch();

        MatchingApplicationResponse result = matchingApplicationService
                .findMatchingApplication(matchingApplicationRequest.memberId());

        assertThat(result.memberId()).isEqualTo(matchingApplicationRequest.memberId());
    }

    private void applyToMatch() {
        given(matchingApplicationValidator.canApplyMatching(any())).willReturn(true);
        matchingApplicationService.saveParticipant(matchingApplicationRequest);
    }
}
