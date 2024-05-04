package com.aliens.backend.board.domain.repository.custom;

import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.domain.*;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public class GreatCustomRepositoryImpl implements GreatCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QBoard qBoard = QBoard.board;
    private final QGreat qGreat = QGreat.great;

    public GreatCustomRepositoryImpl(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<BoardResponse> getGreatBoardPage(final Long memberId, final Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(qBoard, qBoard.member.memberImage)
                .from(qGreat)

                .join(qGreat.board, qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member).fetchJoin()
                .leftJoin(qBoard.member.memberImage)

                .where(qGreat.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().distinct().toList();


        return results.stream()
                .map(tuple -> {
                    Board board = tuple.get(qBoard);
                    return board.getBoardResponse();
                })
                .toList();
    }
}

