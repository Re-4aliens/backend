package com.aliens.backend.mathcing.service.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LanguageQueue {
    private final Map<Language, CandidateGroup> languageQueue;

    private LanguageQueue(final Map<Language, CandidateGroup> languageQueue) {
        this.languageQueue = languageQueue;
    }

    public static LanguageQueue from(final ParticipantGroup participantGroup) {
        Map<Language, CandidateGroup> languageQueue = new HashMap<>();
        Arrays.stream(Language.values()).forEach(language -> languageQueue.put(language, CandidateGroup.init()));

        participantGroup.getParticipants()
                .forEach(participant -> {
                    Language language = participant.getPreferLanguage(MatchingMode.FIRST_PREFER_LANGUAGE);
                    CandidateGroup candidateGroup = languageQueue.get(language);
                    candidateGroup.add(participant);
                });
        return new LanguageQueue(languageQueue);
    }

    public CandidateGroup get(Language language) {
        return languageQueue.get(language);
    }
}
