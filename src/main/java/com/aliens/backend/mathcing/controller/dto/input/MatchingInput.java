package com.aliens.backend.mathcing.controller.dto.input;

import com.aliens.backend.mathcing.service.model.Language;

import static com.aliens.backend.mathcing.controller.dto.request.MatchingRequest.*;

public class MatchingInput {

    public static class MatchingApplicationInput {
        private Language firstPreferLanguage;
        private Language secondPreferLanguage;

        public MatchingApplicationInput(final Language firstPreferLanguage, final Language secondPreferLanguage) {
            this.firstPreferLanguage = firstPreferLanguage;
            this.secondPreferLanguage = secondPreferLanguage;
        }

        public MatchingApplicationRequest toRequest(final Long memberId){
            return new MatchingApplicationRequest(memberId, firstPreferLanguage, secondPreferLanguage);
        }
    }
}
