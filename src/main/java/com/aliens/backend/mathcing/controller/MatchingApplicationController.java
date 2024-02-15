package com.aliens.backend.mathcing.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.MatchingSuccess;
import com.aliens.backend.global.validator.LanguageCheck;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingApplicationResponse;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matchings")
public class MatchingApplicationController {
    private final MatchingApplicationService matchingApplicationService;

    public MatchingApplicationController(final MatchingApplicationService matchingApplicationService) {
        this.matchingApplicationService = matchingApplicationService;
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
}
