package com.aliens.backend.mathcing.business.type;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.service.model.CandidateGroup;
import com.aliens.backend.mathcing.service.model.LanguageQueue;
import com.aliens.backend.mathcing.service.model.ParticipantGroup;

public class SpecialType implements MatchingType {
    private final MatchingRuleProperties matchingRuleProperties;

    public SpecialType(final MatchingRuleProperties matchingRuleProperties) {
        this.matchingRuleProperties = matchingRuleProperties;
    }

    @Override
    public void doMatch(final ParticipantGroup participantGroup, final LanguageQueue languageQueue) {
        ParticipantGroup participants = participantGroup.getParticipantsLessThan(matchingRuleProperties.getMaxPartners());
        participants.updateToSpecialRelationshipMode();
        participants.matchAllWith(CandidateGroup.of(participants));
    }
}
