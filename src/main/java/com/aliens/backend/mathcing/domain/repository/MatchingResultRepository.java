package com.aliens.backend.mathcing.domain.repository;

import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.id.MatchingResultId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingResultRepository extends JpaRepository<MatchingResult, MatchingResultId> {
    @Query("SELECT mr FROM MatchingResult mr WHERE mr.id.matchingRound.round = :round")
    List<MatchingResult> findAllByRound(Long round);

    @Query("SELECT mr FROM MatchingResult mr " +
            "WHERE mr.id.matchingRound.round = :round AND mr.id.matchingMember.id = :memberId")
    List<MatchingResult> findAllByMatchingRoundAndMemberId(Long round, Long memberId);
}
