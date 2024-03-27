package com.aliens.backend.mathcing.business;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.business.model.*;
import com.aliens.backend.mathcing.controller.dto.request.MatchingOperateRequest;
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

    public void operateMatching(final MatchingOperateRequest matchingOperateRequest) {
        initialize(matchingOperateRequest);

        matchingTypeGroup.matchParticipants(participantGroup, languageQueue);
    }

    public List<Participant> getMatchedParticipants() {
        return participantGroup.getParticipants();
    }

    private void initialize(final MatchingOperateRequest matchingOperateRequest) {
        participantGroup = ParticipantGroup.from(matchingOperateRequest, matchingRuleProperties);
        languageQueue = LanguageQueue.classifyByLanguage(participantGroup);
        matchingTypeGroup = MatchingTypeGroup.init(matchingRuleProperties);
    }
}
