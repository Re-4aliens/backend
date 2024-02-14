package com.aliens.backend.mathcing.controller.dto.request;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.service.model.Language;

public class MatchingRequest {
    public record MatchingApplicationRequest(
            Long memberId,
            Language firstPreferLanguage,
            Language secondPreferLanguage
    ) {
    }
}
