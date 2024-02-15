package com.aliens.backend.mathcing.business.type;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.business.model.CandidateGroup;
import com.aliens.backend.mathcing.business.model.LanguageQueue;
import com.aliens.backend.mathcing.business.model.ParticipantGroup;

public class SpecialType implements MatchingType {
    private final MatchingRuleProperties matchingRuleProperties;

    public SpecialType(final MatchingRuleProperties matchingRuleProperties) {
        this.matchingRuleProperties = matchingRuleProperties;
    }

    @Override
    public void doMatch(final ParticipantGroup participantGroup, final LanguageQueue languageQueue) {
        ParticipantGroup remainedParticipants = participantGroup.getParticipantsLessThan(matchingRuleProperties.getMaxPartners());
        remainedParticipants.updateToSpecialRelationshipMode();
        remainedParticipants.matchAllWith(CandidateGroup.of(remainedParticipants));
    }
}
