package com.aliens.backend.board.domain.repository;

import com.aliens.backend.board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImageRepository  extends JpaRepository<BoardImage, Long>  {
}
