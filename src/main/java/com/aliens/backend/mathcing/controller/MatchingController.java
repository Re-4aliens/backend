package com.aliens.backend.mathcing.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.success.MatchingSuccessCode;
import com.aliens.backend.global.success.SuccessResponseWithoutResult;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> applyMatch(@Login LoginMember loginMember,
                                        @RequestBody MatchingApplicationInput matchingApplicationInput) {
        matchingApplicationService.saveParticipant(matchingApplicationInput.toRequest(loginMember.memberId()));
        return SuccessResponseWithoutResult.toResponseEntity(MatchingSuccessCode.APPLY_MATCHING_SUCCESS);
    }
}
