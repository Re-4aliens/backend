package com.aliens.backend.board.domain.repository;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.marketInfo LEFT JOIN FETCH b.boardImages")
    List<Board> findAlLWithMarketInfo();

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.boardImages")
    List<Board> findAllWithBoardImage();

    Optional<Board> findFirstByMemberOrderById(Member member);
}
