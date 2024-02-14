package com.aliens.backend.mathcing.business.model;

import java.util.*;

public enum Language {
    KOREAN,
    ENGLISH,
    JAPANESE,
    CHINESE
    ;

    public static Map<Language, Queue<Participant>> createQueueWith(final List<Participant> participants) {
        Map<Language, Queue<Participant>> languageQueue = new HashMap<>();
        for (Language language : values()) {
            languageQueue.put(language, new LinkedList<>());
        }

        for (Participant participant : participants) {
            languageQueue.get(participant.getPreferLanguage(MatchingMode.FIRST_PREFER_LANGUAGE)).add(participant);
        }

        return languageQueue;
    }
}
