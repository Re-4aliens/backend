package com.aliens.backend.mathcing.business;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.util.MatchingConverter;
import com.aliens.backend.mathcing.util.MatchingQueueBuilder;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.service.model.Language;
import com.aliens.backend.mathcing.service.model.Participant;
import com.aliens.backend.mathcing.service.model.MatchingMode;
import com.aliens.backend.mathcing.service.model.Relationship;
import com.aliens.backend.mathcing.validator.MatchingBusinessValidator;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MatchingBusiness {
    private final MatchingConverter matchingConverter;
    private final MatchingQueueBuilder matchingQueueBuilder;
    private final MatchingBusinessValidator matchingBusinessValidator;
    private final MatchingRuleProperties matchingRuleProperties;

    private List<Participant> participants = new ArrayList<>();
    private Map<Language, Queue<Participant>> languageQueueWithParticipants = new HashMap<>();
    private Relationship relationship;

    public MatchingBusiness(final MatchingConverter matchingConverter,
                            final MatchingQueueBuilder matchingQueueBuilder,
                            final MatchingBusinessValidator matchingBusinessValidator,
                            final MatchingRuleProperties matchingRuleProperties) {
        this.matchingConverter = matchingConverter;
        this.matchingQueueBuilder = matchingQueueBuilder;
        this.matchingBusinessValidator = matchingBusinessValidator;
        this.matchingRuleProperties = matchingRuleProperties;
    }

    private void initialize(List<MatchingApplication> matchingApplications) {
        participants = matchingConverter.toParticipantList(matchingApplications);
        languageQueueWithParticipants = matchingQueueBuilder.buildLanguageQueues(participants);
        relationship = Relationship.NORMAL;
    }

    public List<Participant> operateMatching(List<MatchingApplication> matchingApplications) {
        initialize(matchingApplications);

        matchParticipantsWith(MatchingMode.FIRST_PREFER_LANGUAGE);
        matchParticipantsWith(MatchingMode.SECOND_PREFER_LANGUAGE);
        matchParticipantsWith(MatchingMode.RANDOM);
        matchParticipantsWith(MatchingMode.SPECIAL);

        return participants;
    }

    private void matchParticipantsWith(MatchingMode matchingMode) {
        List<Participant> participants = null;
        if (matchingMode.equals(MatchingMode.FIRST_PREFER_LANGUAGE)) {
            participants = this.participants;
        }
        if (matchingMode.equals(MatchingMode.SECOND_PREFER_LANGUAGE)) {
            participants = getParticipantsLessThan(matchingRuleProperties.getMaxNormalPartners());
            languageQueueWithParticipants = matchingQueueBuilder.buildLanguageQueues(this.participants);
        }
        if (matchingMode.equals(MatchingMode.RANDOM)) {
            relationship = Relationship.SPECIAL;
            participants = getParticipantsLessThan(matchingRuleProperties.getMaxNormalPartners());
        }
        if (matchingMode.equals(MatchingMode.SPECIAL)) {
            participants = getParticipantsLessThan(matchingRuleProperties.getMaxPartners());
        }
        matchWith(matchingMode, participants);
    }

    private void matchWith(MatchingMode matchingMode, List<Participant> participants) {
        Queue<Participant> candidates = null;
        if (matchingMode.equals(MatchingMode.RANDOM)) {
            candidates = new LinkedList<>(getParticipantsLessThan(matchingRuleProperties.getMaxPartners()));
        }
        if (matchingMode.equals(MatchingMode.SPECIAL)) {
            candidates = new LinkedList<>(participants);
        }
        for (Participant participant : participants) {
            if (matchingMode.equals(MatchingMode.FIRST_PREFER_LANGUAGE) ||
                    matchingMode.equals(MatchingMode.SECOND_PREFER_LANGUAGE)) {
                candidates = languageQueueWithParticipants.get(participant.getPreferLanguage(matchingMode));
            }
            tryMatchBetween(participant, candidates);
        }
    }

    private void tryMatchBetween(Participant participant, Queue<Participant> candidates) {
        int tries = 0;
        while (!matchingBusinessValidator.isExceededMaxPartners(relationship, participant) &&
                !matchingBusinessValidator.isExceedMaxTries(tries) && !candidates.isEmpty()) {
            Participant partner = candidates.poll();
            tries++;
            if (matchingBusinessValidator.isValidMatching(relationship, participant, partner)) {
                addMatching(participant, partner);
                if (!matchingBusinessValidator.isExceededMaxPartners(relationship, partner)) {
                    candidates.add(partner);
                }
            } else {
                candidates.add(partner);
            }
        }
    }

    private void addMatching(Participant participant, Participant partner) {
        participant.addPartner(relationship, partner.memberId());
        partner.addPartner(relationship, participant.memberId());
    }

    private List<Participant> getParticipantsLessThan(int numberOfPartner) {
        return participants.stream()
                .filter(participant -> participant.getNumberOfPartners() < numberOfPartner)
                .collect(Collectors.toList());
    }

    public List<Participant> getMatchedParticipants() {
        return participants;
    }
}
