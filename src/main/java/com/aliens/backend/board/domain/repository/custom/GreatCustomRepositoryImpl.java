package com.aliens.backend.board.domain.repository.custom;

import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.domain.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        JPAQuery<Tuple> query = queryFactory
                .select(qBoard, qBoard.member.memberImage)
                .from(qGreat)

                .join(qGreat.board, qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member).fetchJoin()
                .leftJoin(qBoard.member.memberImage)

                .where(qGreat.member.id.eq(memberId));

        List<Tuple> results = getPagingEntity(pageable, query);
        return getBoardResponses(results);
    }

    private List<Tuple> getPagingEntity(Pageable pageable, JPAQuery<Tuple> query) {
        if (pageable.getSort().isSorted()) {
            Sort.Order sortOrder = pageable.getSort().iterator().next();
            PathBuilder pathBuilder = new PathBuilder(qBoard.getType(), qBoard.getMetadata());
            Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;
            query.orderBy(new OrderSpecifier<>(direction, pathBuilder.get(sortOrder.getProperty())));
        }

        List<Tuple> results = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().distinct().toList();
        return results;
    }

    private List<BoardResponse> getBoardResponses(List<Tuple> results) {
        return results.stream()
                .map(tuple -> {
                    Board board = tuple.get(qBoard);
                    return board.getBoardResponse();
                })
                .toList();
    }
}

