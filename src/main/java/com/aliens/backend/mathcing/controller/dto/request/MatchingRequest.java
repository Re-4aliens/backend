package com.aliens.backend.mathcing.controller.dto.request;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.service.model.Language;

public class MatchingRequest {
    public static class MatchingApplicationRequest {
        private Long memberId;
        private Language firstPreferLanguage;
        private Language secondPreferLanguage;

        public MatchingApplicationRequest(final Long memberId,
                                          final Language firstPreferLanguage,
                                          final Language secondPreferLanguage) {
            this.memberId = memberId;
            this.firstPreferLanguage = firstPreferLanguage;
            this.secondPreferLanguage = secondPreferLanguage;
        }

        public MatchingApplication toEntity(MatchingRound matchingRound) {
            return MatchingApplication.of(matchingRound, memberId, firstPreferLanguage, secondPreferLanguage);
        }

        public Long getMemberId() {
            return memberId;
        }

        public Language getFirstPreferLanguage() {
            return firstPreferLanguage;
        }

        public Language getSecondPreferLanguage() {
            return secondPreferLanguage;
        }
    }
}
