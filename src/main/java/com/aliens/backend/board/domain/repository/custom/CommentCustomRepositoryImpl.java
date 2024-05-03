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
public class CommentCustomRepositoryImpl implements CommentCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QBoard qBoard = QBoard.board;
    private final QComment qComment = QComment.comment;

    public CommentCustomRepositoryImpl(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<BoardResponse> getCommentedBoardPage(final Long memberId, final Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(qBoard, qBoard.member.memberImage)
                .from(qComment)
                .join(qComment.board, qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member).fetchJoin()
                .leftJoin(qBoard.member.memberImage)

                .where(qComment.member.id.eq(memberId))
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

    @Override
    public List<Comment> getCommentsByBoardId(final Long boardId) {
        List<Tuple> results = queryFactory
                .select(qComment, qComment.member.memberImage)
                .from(qComment)
                .leftJoin(qComment.member).fetchJoin()
                .leftJoin(qComment.member.memberImage)

                .where(qComment.board.id.eq(boardId))
                .fetch()
                .stream().distinct().toList();


        return results.stream()
                .map(tuple -> tuple.get(qComment))
                .toList();
    }
}

