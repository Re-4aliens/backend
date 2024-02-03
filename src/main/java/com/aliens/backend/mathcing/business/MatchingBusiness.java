package com.aliens.backend.mathcing.business;

import com.aliens.backend.global.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.business.util.MatchingConverter;
import com.aliens.backend.mathcing.business.util.MatchingQueueBuilder;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.model.Language;
import com.aliens.backend.mathcing.service.model.Participant;
import com.aliens.backend.mathcing.service.model.PreferLanguage;
import com.aliens.backend.mathcing.service.model.Relationship;
import com.aliens.backend.mathcing.validator.MatchingBusinessValidator;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MatchingBusiness {
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final MatchingRoundRepository matchingRoundRepository;
    private final MatchingConverter matchingConverter;
    private final MatchingQueueBuilder matchingQueueBuilder;
    private final MatchingBusinessValidator matchingBusinessValidator;
    private final MatchingRuleProperties matchingRuleProperties;

    private List<Participant> participants = new ArrayList<>();
    private Map<Language, Queue<Participant>> languageQueueWithParticipants = new HashMap<>();
    private Relationship relationship;

    public MatchingBusiness(final MatchingApplicationRepository matchingApplicationRepository,
                            final MatchingRoundRepository matchingRoundRepository,
                            final MatchingConverter matchingConverter,
                            final MatchingQueueBuilder matchingQueueBuilder,
                            final MatchingBusinessValidator matchingBusinessValidator,
                            final MatchingRuleProperties matchingRuleProperties) {
        this.matchingApplicationRepository = matchingApplicationRepository;
        this.matchingRoundRepository = matchingRoundRepository;
        this.matchingConverter = matchingConverter;
        this.matchingQueueBuilder = matchingQueueBuilder;
        this.matchingBusinessValidator = matchingBusinessValidator;
        this.matchingRuleProperties = matchingRuleProperties;
    }

    @PostConstruct
    private void initialize() {
        MatchingRound currentRound = matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
        participants = matchingConverter.toParticipantList(
                matchingApplicationRepository.findAllByMatchingRound(currentRound));
        languageQueueWithParticipants = matchingQueueBuilder.buildLanguageQueues(participants);
        relationship = Relationship.NORMAL;
    }

    @Scheduled(cron = "${matching.round.start}")
    public void operateMatching() {
        matchParticipantsWith(PreferLanguage.FIRST);
        matchParticipantsWith(PreferLanguage.SECOND);

    }

    private void matchParticipantsWith(PreferLanguage preferLanguage) {
        if (preferLanguage.equals(PreferLanguage.FIRST)) {
            matchWith(preferLanguage, participants);
        }
        if (preferLanguage.equals(PreferLanguage.SECOND)) {
            List<Participant> lessMatchedParticipants = getParticipantsLessThan(matchingRuleProperties.getMaxNormalPartners());
            languageQueueWithParticipants = matchingQueueBuilder.buildLanguageQueues(participants);
            matchWith(preferLanguage, lessMatchedParticipants);
        }
    }

    private void matchWith(PreferLanguage preferLanguage, List<Participant> participants) {
        for (Participant participant : participants) {
            Queue<Participant> candidates = languageQueueWithParticipants.get(participant.getPreferLanguage(preferLanguage));
            tryMatchBetween(participant, candidates);
        }
    }

    private void tryMatchBetween(Participant participant, Queue<Participant> candidates) {
        int tries = 0;
        while (matchingBusinessValidator.isExceededMaxNormalPartners(participant) &&
                matchingBusinessValidator.isExceedMaxTries(tries) && !candidates.isEmpty()) {
            Participant partner = candidates.poll();
            tries++;
            if (matchingBusinessValidator.isValidMatching(participant, partner)) {
                addMatching(participant, partner);
                if (!matchingBusinessValidator.isExceededMaxNormalPartners(partner)) {
                    candidates.add(partner);
                }
            } else {
                candidates.add(partner);
            }
        }
    }

    private void addMatching(Participant participant, Participant partner) {
        participant.addPartner(relationship, partner);
        partner.addPartner(relationship, participant);
    }

    private List<Participant> getParticipantsLessThan(int numberOfPartner) {
        return participants.stream()
                .filter(participant -> participant.getNumberOfPartners() < numberOfPartner)
                .collect(Collectors.toList());
    }
}
