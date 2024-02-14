package com.aliens.backend.mathcing.business;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.business.model.*;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MatchingBusiness {
    private final MatchingRuleProperties matchingRuleProperties;

    private MatchingTypeGroup matchingTypeGroup;
    private ParticipantGroup participantGroup;
    private LanguageQueue languageQueue;

    public MatchingBusiness(final MatchingRuleProperties matchingRuleProperties) {
        this.matchingRuleProperties = matchingRuleProperties;
    }

    public void operateMatching(List<MatchingApplication> matchingApplications) {
        initialize(matchingApplications);

        matchingTypeGroup.getMatchingTypes().forEach(matchingType -> matchingType.doMatch(participantGroup, languageQueue));
    }

    public List<Participant> getParticipants() {
        return participantGroup.getParticipants();
    }

    private void initialize(final List<MatchingApplication> matchingApplications) {
        participantGroup = ParticipantGroup.from(matchingApplications, matchingRuleProperties);
        languageQueue = LanguageQueue.from(participantGroup);
        matchingTypeGroup = MatchingTypeGroup.init(matchingRuleProperties);
    }
}
