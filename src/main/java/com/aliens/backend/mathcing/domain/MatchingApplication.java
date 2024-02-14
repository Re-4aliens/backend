package com.aliens.backend.mathcing.domain;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.domain.id.MatchingApplicationId;
import com.aliens.backend.mathcing.service.model.Language;
import com.aliens.backend.mathcing.service.model.Participant;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;
import java.util.stream.Collectors;

@Entity
public class MatchingApplication {
    @EmbeddedId
    private MatchingApplicationId id;

    @Enumerated(EnumType.STRING)
    private Language firstPreferLanguage;

    @Enumerated(EnumType.STRING)
    private Language secondPreferLanguage;

    protected MatchingApplication() {
    }

    private MatchingApplication(final MatchingApplicationId id,
                               final Language firstPreferLanguage,
                               final Language secondPreferLanguage) {
        this.id = id;
        this.firstPreferLanguage = firstPreferLanguage;
        this.secondPreferLanguage = secondPreferLanguage;
    }

    public Language getFirstPreferLanguage() {
        return firstPreferLanguage;
    }

    public Language getSecondPreferLanguage() {
        return secondPreferLanguage;
    }

    public static MatchingApplication of(final MatchingRound matchingRound,
                                         final Long memberId,
                                         final Language firstPreferLanguage,
                                         final Language secondPreferLanguage) {
        return new MatchingApplication(MatchingApplicationId.of(matchingRound, memberId), firstPreferLanguage, secondPreferLanguage);
    }

    public static MatchingApplication from(final MatchingRound matchingRound,
                                           final LoginMember loginMember,
                                           final MatchingApplicationRequest matchingApplicationRequest) {
        return MatchingApplication.of(matchingRound, loginMember.memberId(),
                matchingApplicationRequest.firstPreferLanguage(),
                matchingApplicationRequest.secondPreferLanguage());
    }

    public static List<Participant> toParticipantList(final List<MatchingApplication> matchingApplications) {
        return matchingApplications.stream()
                .map(Participant::of)
                .collect(Collectors.toList());
    }

    public Long getMemberId() {
        return id.getMemberId();
    }

    public MatchingRound getMatchingRound() {
        return id.getMatchingRound();
    }

    public Long getRound() {
        return getMatchingRound().getRound();
    }

    @Override
    public String toString() {
        return "MatchingApplication{" +
                "matchingApplicationId=" + id +
                ", firstLanguage=" + firstPreferLanguage +
                ", secondLanguage=" + secondPreferLanguage +
                '}';
    }
}

