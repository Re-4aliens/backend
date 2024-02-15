package com.aliens.backend.mathcing.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.success.MatchingSuccess;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingApplicationResponse;
import com.aliens.backend.mathcing.controller.dto.response.MatchingResultResponse;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.MatchingService;
import com.aliens.backend.global.validator.LanguageCheck;
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
    public SuccessResponse<String> applyMatch(final @Login LoginMember loginMember,
                                              final @RequestBody @LanguageCheck MatchingApplicationRequest matchingApplicationRequest) {
        return SuccessResponse.of(MatchingSuccess.APPLY_MATCHING_SUCCESS,
                matchingApplicationService.saveParticipant(loginMember, matchingApplicationRequest));
    }

    @GetMapping("/applications")
    public SuccessResponse<MatchingApplicationResponse> getMatchingApplication(final @Login LoginMember loginMember) {
        return SuccessResponse.of(MatchingSuccess.GET_MATCHING_APPLICATION_STATUS_SUCCESS,
                matchingApplicationService.findMatchingApplication(loginMember));
    }

    @DeleteMapping("/applications")
    public SuccessResponse<String> cancelMatchingApplication(final @Login LoginMember loginMember) {
        return SuccessResponse.of(MatchingSuccess.CANCEL_MATCHING_APPLICATION_SUCCESS,
                matchingApplicationService.cancelMatchingApplication(loginMember));
    }

    @GetMapping("/partners")
    public SuccessResponse<List<MatchingResultResponse>> getMatchingPartners(final @Login LoginMember loginMember) {
        return SuccessResponse.of(MatchingSuccess.GET_MATCHING_PARTNERS_SUCCESS,
                matchingService.findMatchingResult(loginMember));
    }
}
