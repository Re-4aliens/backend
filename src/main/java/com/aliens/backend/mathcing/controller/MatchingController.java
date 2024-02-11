package com.aliens.backend.mathcing.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.success.MatchingSuccessCode;
import com.aliens.backend.global.success.SuccessResponse;
import com.aliens.backend.global.success.SuccessResponseWithoutResult;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingApplicationResponse;
import com.aliens.backend.mathcing.controller.dto.response.MatchingResultResponse;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.MatchingService;
import com.aliens.backend.mathcing.util.validator.LanguageCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matchings")
public class MatchingController {
    private final MatchingApplicationService matchingApplicationService;
    private final MatchingService matchingService;

    public MatchingController(final MatchingApplicationService matchingApplicationService,
                              final MatchingService matchingService) {
        this.matchingApplicationService = matchingApplicationService;
        this.matchingService = matchingService;
    }

    @PostMapping("/applications")
    public ResponseEntity<?> applyMatch(final @Login LoginMember loginMember,
                                        final @RequestBody @LanguageCheck MatchingApplicationRequest matchingApplicationRequest) {
        matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest);
        return SuccessResponseWithoutResult.toResponseEntity(MatchingSuccessCode.APPLY_MATCHING_SUCCESS);
    }

    @GetMapping("/applications")
    public ResponseEntity<?> getMatchingApplication(final @Login LoginMember loginMember) {
        MatchingApplicationResponse matchingApplicationResponse = matchingApplicationService.findMatchingApplication(loginMember);
        return SuccessResponse.toResponseEntity(MatchingSuccessCode.GET_MATCHING_APPLICATION_STATUS_SUCCESS,
                matchingApplicationResponse);
    }

    @DeleteMapping("/applications")
    public ResponseEntity<?> cancelMatchingApplication(final @Login LoginMember loginMember) {
        matchingApplicationService.deleteMatchingApplication(loginMember);
        return SuccessResponseWithoutResult.toResponseEntity(MatchingSuccessCode.CANCEL_MATCHING_APPLICATION_SUCCESS);
    }

    @GetMapping("/partners")
    public ResponseEntity<?> getMatchingPartners(final @Login LoginMember loginMember) {
        List<MatchingResultResponse> matchingResultResponses = matchingService.findMatchingResult(loginMember);
        return SuccessResponse.toResponseEntity(MatchingSuccessCode.GET_MATCHING_PARTNERS_SUCCESS,
                matchingResultResponses);
    }
}
