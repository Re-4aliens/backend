package com.aliens.backend.mathcing.business.type;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.service.model.LanguageQueue;
import com.aliens.backend.mathcing.service.model.MatchingMode;
import com.aliens.backend.mathcing.service.model.ParticipantGroup;

public class SecondPreferLanguageType implements MatchingType {
    private final MatchingRuleProperties matchingRuleProperties;

    public SecondPreferLanguageType(final MatchingRuleProperties matchingRuleProperties) {
        this.matchingRuleProperties = matchingRuleProperties;
    }

    @Override
    public void doMatch(final ParticipantGroup participantGroup, final LanguageQueue languageQueue) {
        ParticipantGroup participants = participantGroup.getParticipantsLessThan(matchingRuleProperties.getMaxNormalPartners());
        participants.matchEachWith(languageQueue, MatchingMode.SECOND_PREFER_LANGUAGE);
    }
}
