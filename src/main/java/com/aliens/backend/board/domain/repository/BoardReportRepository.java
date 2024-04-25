package com.aliens.backend.board.domain.repository;

import com.aliens.backend.board.domain.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
    Long countByBoardId(Long boardId);
}
