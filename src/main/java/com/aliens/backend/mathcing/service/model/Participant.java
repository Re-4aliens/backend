package com.aliens.backend.mathcing.service.model;

import java.util.List;

public class Participant {
    private Long memberId;
    private Language firstPreferLanguage;
    private Language secondPreferLanguage;
    private List<Participant> partners;

    public Participant(final Long memberId, final Language firstPreferLanguage, final Language secondPreferLanguage, final List<Participant> partners) {
        this.memberId = memberId;
        this.firstPreferLanguage = firstPreferLanguage;
        this.secondPreferLanguage = secondPreferLanguage;
        this.partners = partners;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Language getPreferLanguage(PreferLanguage preferLanguage) {
        if (preferLanguage.equals(PreferLanguage.FIRST)) {
            return firstPreferLanguage;
        }
        if (preferLanguage.equals(PreferLanguage.SECOND)) {
            return secondPreferLanguage;
        }
        return Language.KOREAN;
    }

    public List<Participant> getPartners() {
        return partners;
    }

    public int getNumberOfPartners() {
        return partners.size();
    }

    public void addPartner(Participant participant) {
        partners.add(participant);
    }
}
