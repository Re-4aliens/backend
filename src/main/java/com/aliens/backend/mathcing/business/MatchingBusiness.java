package com.aliens.backend.mathcing.business;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.service.model.Language;
import com.aliens.backend.mathcing.service.model.Participant;
import com.aliens.backend.mathcing.service.model.MatchingMode;
import com.aliens.backend.mathcing.service.model.Relationship;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MatchingBusiness {
    private final MatchingRuleProperties matchingRuleProperties;

    private List<Participant> participants = new ArrayList<>();
    private Map<Language, Queue<Participant>> languageQueueWithParticipants = new HashMap<>();
    private Relationship relationship;

    public MatchingBusiness(final MatchingRuleProperties matchingRuleProperties) {
        this.matchingRuleProperties = matchingRuleProperties;
    }

    public List<Participant> operateMatching(final List<MatchingApplication> matchingApplications) {
        initialize(matchingApplications);

        matchParticipantsWith(MatchingMode.FIRST_PREFER_LANGUAGE);
        matchParticipantsWith(MatchingMode.SECOND_PREFER_LANGUAGE);
        matchParticipantsWith(MatchingMode.RANDOM);
        matchParticipantsWith(MatchingMode.SPECIAL);

        return participants;
    }

    private void initialize(final List<MatchingApplication> matchingApplications) {
        participants = MatchingApplication.toParticipantList(matchingApplications);
        languageQueueWithParticipants = Language.createQueueWith(participants);
        relationship = Relationship.NORMAL;
    }

    private void matchParticipantsWith(final MatchingMode matchingMode) {
        List<Participant> participants = null;
        if (matchingMode.equals(MatchingMode.FIRST_PREFER_LANGUAGE)) {
            participants = this.participants;
        }
        if (matchingMode.equals(MatchingMode.SECOND_PREFER_LANGUAGE)) {
            participants = getParticipantsLessThan(matchingRuleProperties.getMaxNormalPartners());
            languageQueueWithParticipants = Language.createQueueWith(this.participants);
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

    private void matchWith(final MatchingMode matchingMode, final List<Participant> participants) {
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

    private void tryMatchBetween(final Participant participant, final Queue<Participant> candidates) {
        int tries = 0;
        while (!isExceededMaxPartners(relationship, participant) && !isExceedMaxTries(tries) && !candidates.isEmpty()) {
            Participant partner = candidates.poll();
            tries++;
            if (isValidMatching(relationship, participant, partner)) {
                addMatching(participant, partner);
                if (!isExceededMaxPartners(relationship, partner)) {
                    candidates.add(partner);
                }
            } else {
                candidates.add(partner);
            }
        }
    }

    private void addMatching(final Participant participant, final Participant partner) {
        participant.addPartner(relationship, partner.memberId());
        partner.addPartner(relationship, participant.memberId());
    }

    private List<Participant> getParticipantsLessThan(int numberOfPartner) {
        return participants.stream()
                .filter(participant -> participant.getNumberOfPartners() < numberOfPartner)
                .collect(Collectors.toList());
    }

    private boolean isValidMatching(final Relationship relationship,
                                   final Participant participant,
                                   final Participant partner) {
        return participant != partner &&
                !participant.isPartnerWith(partner) &&
                !partner.isPartnerWith(participant) &&
                !isExceededMaxPartners(relationship, partner);
    }

    private boolean isExceededMaxPartners(final Relationship relationship, final Participant participant) {
        if (relationship.equals(Relationship.NORMAL)) {
            return participant.getNumberOfPartners() >= matchingRuleProperties.getMaxNormalPartners(); // 4
        }
        return participant.getNumberOfPartners() >= matchingRuleProperties.getMaxPartners(); // 5
    }

    private boolean isExceedMaxTries(int tries) {
        return tries > matchingRuleProperties.getMaxTries();
    }
}
