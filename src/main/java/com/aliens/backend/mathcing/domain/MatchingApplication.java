package com.aliens.backend.mathcing.domain;

import com.aliens.backend.mathcing.controller.dto.request.MatchingRequest;
import com.aliens.backend.mathcing.domain.id.MatchingApplicationId;
import com.aliens.backend.mathcing.service.model.Language;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import static com.aliens.backend.mathcing.controller.dto.request.MatchingRequest.*;

@Entity
public class MatchingApplication {
    @EmbeddedId
    private MatchingApplicationId matchingApplicationId;

    @Enumerated(EnumType.STRING)
    private Language firstPreferLanguage;

    @Enumerated(EnumType.STRING)
    private Language secondPreferLanguage;

    protected MatchingApplication() {
    }

    public MatchingApplication(final MatchingApplicationId matchingApplicationId,
                               final Language firstPreferLanguage,
                               final Language secondPreferLanguage) {
        this.matchingApplicationId = matchingApplicationId;
        this.firstPreferLanguage = firstPreferLanguage;
        this.secondPreferLanguage = secondPreferLanguage;
    }

    public MatchingApplicationId getMatchingApplicationId() {
        return matchingApplicationId;
    }

    public Language getFirstPreferLanguage() {
        return firstPreferLanguage;
    }

    public Language getSecondPreferLanguage() {
        return secondPreferLanguage;
    }

    public static MatchingApplication of(final MatchingRound matchingRound,
                                         final Long memberId,
                                         final Language firstPreferLanguage, final Language secondPreferLanguage) {
        return new MatchingApplication(
                new MatchingApplicationId(matchingRound, memberId),
                firstPreferLanguage, secondPreferLanguage);
    }

    @Override
    public String toString() {
        return "MatchingApplication{" +
                "matchingApplicationId=" + matchingApplicationId +
                ", firstLanguage=" + firstPreferLanguage +
                ", secondLanguage=" + secondPreferLanguage +
                '}';
    }
}

