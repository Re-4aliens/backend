package com.aliens.backend.mathcing.domain.repository;

import com.aliens.backend.mathcing.domain.MatchingRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingRoundRepository extends JpaRepository<MatchingRound, Long> {
    @Query("SELECT mr FROM MatchingRound mr ORDER BY mr.round DESC")
    Optional<MatchingRound> findCurrentRound();
}
