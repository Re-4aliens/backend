package com.aliens.backend.mathcing.controller.dto.input;

import com.aliens.backend.mathcing.service.model.Language;

import static com.aliens.backend.mathcing.controller.dto.request.MatchingRequest.*;

public class MatchingInput {
    public record MatchingApplicationInput(
            Language firstPreferLanguage,
            Language secondPreferLanguage) {
        public MatchingApplicationRequest toRequest(final Long memberId){
            return new MatchingApplicationRequest(memberId, firstPreferLanguage, secondPreferLanguage);
        }
    }
}
