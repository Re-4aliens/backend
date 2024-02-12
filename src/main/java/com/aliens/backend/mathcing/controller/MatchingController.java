package com.aliens.backend.mathcing.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.success.MatchingSuccess;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.mathcing.controller.dto.response.MatchingResponse;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.MatchingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.aliens.backend.mathcing.controller.dto.input.MatchingInput.*;

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
                                        final @RequestBody MatchingApplicationInput input) {

        return SuccessResponse.of(
                MatchingSuccess.APPLY_MATCHING_SUCCESS,
                matchingApplicationService.saveParticipant(input.toRequest(loginMember.memberId()))
        );
    }

    @GetMapping("/applications")
    public SuccessResponse<MatchingResponse.MatchingApplicationResponse> getMatchingApplication(final @Login LoginMember loginMember) {

        return SuccessResponse.of(
                MatchingSuccess.GET_MATCHING_APPLICATION_STATUS_SUCCESS,
                matchingApplicationService.findMatchingApplication(loginMember.memberId())
        );
    }

    @DeleteMapping("/applications")
    public SuccessResponse<String> cancelMatchingApplication(final @Login LoginMember loginMember) {

        return SuccessResponse.of(
                MatchingSuccess.CANCEL_MATCHING_APPLICATION_SUCCESS,
                matchingApplicationService.deleteMatchingApplication(loginMember.memberId())
        );
    }

    @GetMapping("/partners")
    public SuccessResponse<List<MatchingResponse.MatchingResultResponse>> getMatchingPartners(final @Login LoginMember loginMember) {

        return SuccessResponse.of(
                MatchingSuccess.GET_MATCHING_PARTNERS_SUCCESS,
                matchingService.findMatchingResult(loginMember.memberId())
        );
    }
}
