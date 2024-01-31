package com.aliens.backend.matching.unit.service;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.mathcing.controller.dto.request.MatchingRequest;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.model.Language;
import com.aliens.backend.mathcing.validator.MatchingApplicationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.aliens.backend.mathcing.controller.dto.request.MatchingRequest.*;
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
    @Transactional
    void setUp() {
        LocalDateTime monday = LocalDateTime.of(2024, 1, 29, 0, 0);
        matchingRoundRepository.save(MatchingRound.of(monday, matchingTimeProperties));

        matchingApplicationRequest = new MatchingApplicationRequest(1L, Language.KOREAN, Language.ENGLISH);
        currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    @Test
    @DisplayName("매칭 신청 단위 테스트")
    void applyMatchTest() {
        given(matchingApplicationValidator.canApplyMatching(currentRound)).willReturn(true);

        matchingApplicationService.saveParticipant(matchingApplicationRequest);


    }
}
