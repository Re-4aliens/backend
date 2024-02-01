package com.aliens.backend.mathcing.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.success.MatchingSuccessCode;
import com.aliens.backend.global.success.SuccessResponse;
import com.aliens.backend.global.success.SuccessResponseWithoutResult;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.aliens.backend.mathcing.controller.dto.input.MatchingInput.*;

@RestController
@RequestMapping("/matchings")
public class MatchingController {
    private final MatchingApplicationService matchingApplicationService;

    @Autowired
    public MatchingController(final MatchingApplicationService matchingApplicationService) {
        this.matchingApplicationService = matchingApplicationService;
    }

    @PostMapping("/applications")
    public ResponseEntity<?> applyMatch(final @Login LoginMember loginMember,
                                        final @RequestBody MatchingApplicationInput matchingApplicationInput) {
        matchingApplicationService.saveParticipant(matchingApplicationInput.toRequest(loginMember.memberId()));
        return SuccessResponseWithoutResult.toResponseEntity(MatchingSuccessCode.APPLY_MATCHING_SUCCESS);
    }

    @GetMapping("/applications")
    public ResponseEntity<?> getMatchingApplication(final @Login LoginMember loginMember) {
        return SuccessResponse.toResponseEntity(MatchingSuccessCode.GET_MATCHING_APPLICATION_STATUS_SUCCESS,
                matchingApplicationService.findMatchingApplication(loginMember.memberId()));
    }

    @DeleteMapping("/applications")
    public ResponseEntity<?> cancelMatchingApplication(final @Login LoginMember loginMember) {
        matchingApplicationService.deleteMatchingApplication(loginMember.memberId());
        return SuccessResponseWithoutResult.toResponseEntity(MatchingSuccessCode.CANCEL_MATCHING_APPLICATION_SUCCESS);
    }
}
