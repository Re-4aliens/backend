package com.aliens.backend.mathcing.domain.repository;

import com.aliens.backend.mathcing.domain.MatchingRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingRoundRepository extends JpaRepository<MatchingRound, Long> {
    @Query("SELECT mr FROM MatchingRound mr WHERE mr.round = (SELECT MAX(mr.round) FROM MatchingRound mr)")
    Optional<MatchingRound> findCurrentRound();

    Optional<MatchingRound> findMatchingRoundByRound(Long round);
}
