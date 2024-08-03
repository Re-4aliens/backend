package com.aliens.backend.mathcing.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.MatchingSuccess;
import com.aliens.backend.mathcing.controller.dto.response.MatchingResultResponse;
import com.aliens.backend.mathcing.service.MatchingProcessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/matchings")
public class MatchingProcessController {
    private final MatchingProcessService matchingProcessService;

    public MatchingProcessController(final MatchingProcessService matchingProcessService) {
        this.matchingProcessService = matchingProcessService;
    }

    @GetMapping("/partners")
    public SuccessResponse<List<MatchingResultResponse>> getMatchingPartners(final @Login LoginMember loginMember) {
        return SuccessResponse.of(MatchingSuccess.GET_MATCHING_PARTNERS_SUCCESS,
                matchingProcessService.findMatchingResult(loginMember));
    }
}
