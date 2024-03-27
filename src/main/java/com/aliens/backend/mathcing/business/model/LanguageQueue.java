package com.aliens.backend.mathcing.business.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageQueue {
    private final Map<Language, CandidateGroup> languageQueue;

    private LanguageQueue(final Map<Language, CandidateGroup> languageQueue) {
        this.languageQueue = languageQueue;
    }

    public static LanguageQueue classifyByLanguage(final ParticipantGroup participantGroup) {
        Map<Language, CandidateGroup> languageQueue = createEmptyLanguageQueues();
        classifyParticipantsByLanguage(languageQueue, participantGroup);

        return new LanguageQueue(languageQueue);
    }

    public CandidateGroup getCandidateGroupByLanguage(final Language language) {
        return languageQueue.get(language);
    }

    private static Map<Language, CandidateGroup> createEmptyLanguageQueues() {
        Map<Language, CandidateGroup> languageQueue = new HashMap<>();
        List<Language> languages = List.of(Language.values());
        languages.forEach(language -> languageQueue.put(language, CandidateGroup.initWithEmpty()));
        return languageQueue;
    }

    private static void classifyParticipantsByLanguage(final Map<Language, CandidateGroup> languageQueue,
                                                       final ParticipantGroup participantGroup) {
        for (Language language : Language.values()) {
            List<Participant> participants = participantGroup.getParticipantsByLanguage(language);
            CandidateGroup candidateGroup = languageQueue.get(language);
            candidateGroup.addAll(participants);
        }
    }
}
