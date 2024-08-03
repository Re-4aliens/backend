package com.aliens.backend.mathcing.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.mathcing.business.model.Language;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.domain.id.MatchingApplicationId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

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
                                         final Member member,
                                         final Language firstPreferLanguage,
                                         final Language secondPreferLanguage) {
        return new MatchingApplication(MatchingApplicationId.of(matchingRound, member), firstPreferLanguage, secondPreferLanguage);
    }

    public static MatchingApplication from(final MatchingRound matchingRound,
                                           final Member member,
                                           final MatchingApplicationRequest matchingApplicationRequest) {
        return MatchingApplication.of(matchingRound, member,
                matchingApplicationRequest.firstPreferLanguage(),
                matchingApplicationRequest.secondPreferLanguage());
    }

    public void modifyTo(final MatchingApplicationRequest matchingApplicationRequest) {
        this.firstPreferLanguage = matchingApplicationRequest.firstPreferLanguage();
        this.secondPreferLanguage = matchingApplicationRequest.secondPreferLanguage();
    }

    public void expireMatch() {
        Member member = getMember();
        member.expireMatch();
    }

    public MatchingApplicationId getId() {
        return id;
    }

    public Member getMember() {
        return id.getMember();
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