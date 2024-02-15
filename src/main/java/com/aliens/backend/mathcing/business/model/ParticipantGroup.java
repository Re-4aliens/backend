package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.domain.MatchingApplication;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipantGroup {
    private final List<Participant> participants;
    private final MatchingRuleProperties matchingRuleProperties;
    private Relationship relationship;

    private ParticipantGroup(final List<Participant> participants,
                             final MatchingRuleProperties matchingRuleProperties) {
        this.participants = participants;
        this.matchingRuleProperties = matchingRuleProperties;
        this.relationship = Relationship.NORMAL;
    }

    public static ParticipantGroup from(final List<MatchingApplication> matchingApplications,
                                        final MatchingRuleProperties matchingRuleProperties) {
        List<Participant> participants = matchingApplications.stream().map(Participant::of).collect(Collectors.toList());
        return new ParticipantGroup(participants, matchingRuleProperties);
    }

    public void matchAllWith(CandidateGroup candidateGroup) {
        for (Participant participant : participants) {
            tryMatchBetween(participant, candidateGroup);
        }
    }

    public void matchEachWith(LanguageQueue languageQueue, MatchingMode matchingMode) {
        for (Participant participant : participants) {
            Language preferLanguage = participant.getPreferLanguage(matchingMode);
            CandidateGroup candidateGroup = languageQueue.get(preferLanguage);
            tryMatchBetween(participant, candidateGroup);
        }
    }

    public void updateToSpecialRelationshipMode() {
        this.relationship = Relationship.SPECIAL;
    }

    public ParticipantGroup getParticipantsLessThan(final int numberOfPartner) {
        return toGroup(participants.stream()
                .filter(participant -> participant.getNumberOfPartners() < numberOfPartner)
                .collect(Collectors.toList()));
    }

    public List<Participant> getParticipantsByLanguage(Language language) {
        return participants.stream().filter(participant -> participant.firstPreferLanguage().equals(language)).toList();
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    private void tryMatchBetween(final Participant participant, final CandidateGroup candidates) {
        int tries = 0;
        while (canContinueMatching(relationship, participant, tries, candidates)) {
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

    private boolean canContinueMatching(final Relationship relationship,
                                        final Participant participant,
                                        int tries,
                                        final CandidateGroup candidates) {
        return !isExceededMaxPartners(relationship, participant) && !isExceedMaxTries(tries) && !candidates.isEmpty();
    }

    private void addMatching(final Participant participant, final Participant partner) {
        participant.addPartner(relationship, partner.memberId());
        partner.addPartner(relationship, participant.memberId());
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

    private ParticipantGroup toGroup(List<Participant> participants) {
        return new ParticipantGroup(participants, matchingRuleProperties);
    }
}
