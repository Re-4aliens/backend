package com.aliens.backend.mathcing.business.util;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.service.model.Participant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatchingConverter {
    public List<Participant> toParticipantList(List<MatchingApplication> matchingApplications) {
        return matchingApplications.stream()
                .map(this::toParticipantList)
                .collect(Collectors.toList());
    }

    private Participant toParticipantList(MatchingApplication matchingApplication) {
        return new Participant(
                matchingApplication.getId().getMemberId(),
                matchingApplication.getFirstPreferLanguage(),
                matchingApplication.getSecondPreferLanguage(),
                new ArrayList<>()
        );
    }
}
