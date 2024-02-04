package com.aliens.backend.mathcing.domain.repository;

import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.domain.id.MatchingResultId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingResultRepository extends JpaRepository<MatchingResult, MatchingResultId> {
}
