package com.aliens.backend.mathcing.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.mathcing.business.model.Participant;
import com.aliens.backend.mathcing.business.model.Partner;
import com.aliens.backend.mathcing.domain.id.MatchingResultId;
import com.aliens.backend.mathcing.business.model.Relationship;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

@Entity
public class MatchingResult implements Persistable<MatchingResultId> {
    @EmbeddedId
    private MatchingResultId id;

    @Enumerated(EnumType.STRING)
    private Relationship relationship;

    protected MatchingResult() {
    }

    private MatchingResult(final MatchingResultId id, final Relationship relationship) {
        this.id = id;
        this.relationship = relationship;
    }

    public static MatchingResult from(final MatchingRound matchingRound,
                                      final Participant participant,
                                      final Partner partner) {
        return new MatchingResult(MatchingResultId.of(matchingRound, participant.member(), partner.member()), partner.relationship());
    }

    public void matchEach() {
        Member matchingMember = getMatchingMember();
        Member matchedMember = getMatchedMember();
        matchingMember.matched();
        matchedMember.matched();
    }

    public void expireMatch() {
        Member matchingMember = getMatchingMember();
        Member matchedMember = getMatchedMember();
        matchingMember.expireMatch();
        matchedMember.expireMatch();
    }

    public MatchingRound getMatchingRound() {
        return id.getMatchingRound();
    }

    public Member getMatchingMember() {
        return id.getMatchingMember();
    }

    public Member getMatchedMember() {
        return id.getMatchedMember();
    }

    public Long getMatchingMemberId() {
        return id.getMatchingMemberId();
    }

    public Long getMatchedMemberId() {
        return id.getMatchedMemberId();
    }

    public Relationship getRelationship() {
        return relationship;
    }

    @Override
    public MatchingResultId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
