package com.aliens.backend.board.domain.repository;

import com.aliens.backend.board.domain.MarketInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketInfoRepository extends JpaRepository<MarketInfo,Long> {
}
