package com.aliens.backend.mathcing.business.util;

import com.aliens.backend.mathcing.service.model.Language;
import com.aliens.backend.mathcing.service.model.Participant;
import com.aliens.backend.mathcing.service.model.MatchingMode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MatchingQueueBuilder {
    public Map<Language, Queue<Participant>> buildLanguageQueues(List<Participant> participants) {
        Map<Language, Queue<Participant>> languageQueue = createQueuesByLanguage();

        for (Participant participant : participants) {
            languageQueue.get(participant.getPreferLanguage(MatchingMode.FIRST_PREFER_LANGUAGE)).add(participant);
        }

        return languageQueue;
    }

    private Map<Language, Queue<Participant>> createQueuesByLanguage() {
        Map<Language, Queue<Participant>> languageQueue = new HashMap<>();
        for (Language language : Language.values()) {
            languageQueue.put(language, new LinkedList<>());
        }
        return languageQueue;
    }
}
