package com.aliens.backend.mathcing.controller.dto.input;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.mathcing.service.model.Language;

import static com.aliens.backend.mathcing.controller.dto.request.MatchingRequest.*;

public class MatchingInput {
    public record MatchingApplicationInput(
            Language firstPreferLanguage,
            Language secondPreferLanguage) {
        public MatchingApplicationRequest toRequest(final Long memberId){
            validateInput();
            return new MatchingApplicationRequest(memberId, firstPreferLanguage, secondPreferLanguage);
        }

        private void validateInput() {
            if (firstPreferLanguage.equals(secondPreferLanguage)) {
                throw new RestApiException(MatchingError.INVALID_LANGUAGE_INPUT);
            }
        }
    }
}
