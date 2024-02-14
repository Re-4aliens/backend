package com.aliens.backend.mathcing.business.type;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.business.model.LanguageQueue;
import com.aliens.backend.mathcing.business.model.MatchingMode;
import com.aliens.backend.mathcing.business.model.ParticipantGroup;

public class FirstPreferLanguageType implements MatchingType {
    private final MatchingRuleProperties matchingRuleProperties;

    public FirstPreferLanguageType(final MatchingRuleProperties matchingRuleProperties) {
        this.matchingRuleProperties = matchingRuleProperties;
    }

    @Override
    public void doMatch(ParticipantGroup participantGroup, LanguageQueue languageQueue) {
        participantGroup.matchEachWith(languageQueue, MatchingMode.FIRST_PREFER_LANGUAGE);
    }
}
