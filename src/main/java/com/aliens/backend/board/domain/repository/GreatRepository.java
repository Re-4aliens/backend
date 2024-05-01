package com.aliens.backend.board.domain.repository;


import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.Great;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GreatRepository extends JpaRepository<Great,Long> {
    long deleteGreatByMemberAndBoard(Member member, Board board);

    boolean existsByMemberAndBoard(Member member, Board board);
}
