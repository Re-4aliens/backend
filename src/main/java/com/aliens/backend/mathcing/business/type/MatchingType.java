package com.aliens.backend.mathcing.business.type;

import com.aliens.backend.mathcing.business.model.LanguageQueue;
import com.aliens.backend.mathcing.business.model.ParticipantGroup;

public interface MatchingType {
    void doMatch(ParticipantGroup participantGroup, LanguageQueue languageQueue);
}
