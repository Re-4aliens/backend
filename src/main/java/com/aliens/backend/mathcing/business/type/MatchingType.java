package com.aliens.backend.mathcing.business.type;

import com.aliens.backend.mathcing.service.model.*;

import java.util.List;
import java.util.Queue;

public interface MatchingType {
    void doMatch(ParticipantGroup participantGroup, LanguageQueue languageQueue);
}


