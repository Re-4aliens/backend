package com.aliens.backend.mathcing.service.model;

import com.aliens.backend.chat.domain.model.MemberPair;
import com.aliens.backend.mathcing.business.model.Participant;
import com.aliens.backend.mathcing.domain.MatchingResult;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MemberPairGroup {
    private final Set<MemberPair> memberPairs;

    public MemberPairGroup(final Set<MemberPair> memberPairs) {
        this.memberPairs = memberPairs;
    }

    public static MemberPairGroup fromParticipants(List<Participant> participants) {
        Set<MemberPair> memberPairs = participants.stream()
                .flatMap(participant -> participant.partners().stream()
                        .map(partner -> new MemberPair(participant.member(), partner.member()))
                ).collect(Collectors.toSet());
        return new MemberPairGroup(memberPairs);
    }

    public static MemberPairGroup fromMatchingResults(List<MatchingResult> matchingResults) {
        Set<MemberPair> memberPairs = matchingResults.stream()
                .map(matchingResult -> new MemberPair(matchingResult.getMatchingMember(), matchingResult.getMatchedMember()))
                .collect(Collectors.toSet());
        return new MemberPairGroup(memberPairs);
    }

    public Set<MemberPair> getMemberPairs() {
        return memberPairs;
    }
}
