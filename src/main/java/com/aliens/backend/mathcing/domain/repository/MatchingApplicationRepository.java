package com.aliens.backend.mathcing.domain.repository;

import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.id.MatchingApplicationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingApplicationRepository extends JpaRepository<MatchingApplication, MatchingApplicationId> {
    @Query("SELECT ma FROM MatchingApplication ma WHERE ma.id.matchingRound = :matchingRound")
    List<MatchingApplication> findAllByMatchingRound(MatchingRound matchingRound);
}
