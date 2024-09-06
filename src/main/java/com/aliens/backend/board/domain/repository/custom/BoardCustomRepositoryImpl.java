package com.aliens.backend.board.domain.repository.custom;

import com.aliens.backend.auth.domain.QMember;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MarketBoardResponse;
import com.aliens.backend.board.domain.*;
import com.aliens.backend.board.domain.enums.BoardCategory;
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
public class BoardCustomRepositoryImpl implements BoardCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QBoard qBoard = QBoard.board;
    private final QMember qMember = QMember.member;

    public BoardCustomRepositoryImpl(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<BoardResponse> getBoardPage(final Pageable pageable) {
        JPAQuery<Tuple> query = queryFactory
                .select(qBoard, qMember, qMember.memberImage)
                .from(qBoard)

                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage);

        List<Tuple> results = getPagingEntity(pageable, query);
        return getBoardResponses(results);
    }

    @Override
    public List<BoardResponse> getBoardPageWithCategory(final BoardCategory category, Pageable pageable) {
        JPAQuery<Tuple> query = queryFactory
                .select(qBoard, qMember, qMember.memberImage)
                .from(qBoard)

                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)

                .where(qBoard.category.eq(category));

        List<Tuple> results = getPagingEntity(pageable, query);
        return getBoardResponses(results);

    }

    @Override
    public List<BoardResponse> searchBoardPageWithKeyword(final String keyword, final Pageable pageable) {
        JPAQuery<Tuple> query = queryFactory
                .select(qBoard, qMember, qMember.memberImage)
                .from(qBoard)

                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)

                .where(qBoard.title.likeIgnoreCase("%" + keyword + "%")
                        .or(qBoard.content.likeIgnoreCase("%" + keyword + "%")));

        List<Tuple> results = getPagingEntity(pageable, query);
        return getBoardResponses(results);
    }

    @Override
    public List<MarketBoardResponse> getMarketBoardPage(final Pageable pageable) {
        JPAQuery<Tuple> query = queryFactory
                .select(qBoard, qBoard.marketInfo, qMember, qMember.memberImage)
                .from(qBoard)

                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)

                .where(qBoard.category.eq(BoardCategory.MARKET));

        List<Tuple> results = getPagingEntity(pageable, query);
        return getMarketBoardResponses(results);
    }

    @Override
    public List<MarketBoardResponse> searchMarketBoardPage(final String keyword, final Pageable pageable) {
        JPAQuery<Tuple> query = queryFactory
                .select(qBoard, qBoard.marketInfo, qMember, qMember.memberImage)
                .from(qBoard)

                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)

                .where(qBoard.category.eq(BoardCategory.MARKET)
                        .and(qBoard.title.likeIgnoreCase("%" + keyword + "%")
                                .or((qBoard.content.likeIgnoreCase("%" + keyword + "%")))));

        List<Tuple> results = getPagingEntity(pageable, query);
        return getMarketBoardResponses(results);
    }

    private List<MarketBoardResponse> getMarketBoardResponses(List<Tuple> results) {
        return results.stream()
                .map(tuple -> {
                    Board board = tuple.get(qBoard);
                    return board.getMarketBoardResponse();
                })
                .toList();
    }

    @Override
    public List<BoardResponse> getMyBoardPage(Long memberId, Pageable pageable) {

        JPAQuery<Tuple> query = queryFactory
                .select(qBoard, qBoard.marketInfo, qMember, qMember.memberImage)
                .from(qBoard)

                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)

                .where(qBoard.member.id.eq(memberId));

        List<Tuple> results = getPagingEntity(pageable, query);
        return getBoardResponses(results);
    }

    public List<BoardResponse> searchBoardPageWithKeywordAndCategory(String keyword,
                                                                     BoardCategory category,
                                                                     Pageable pageable) {
        JPAQuery<Tuple> query = queryFactory
                .select(qBoard, qBoard.marketInfo, qMember, qMember.memberImage)
                .from(qBoard)

                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)

                .where(qBoard.category.eq(category)
                        .and(qBoard.title.likeIgnoreCase("%" + keyword + "%")
                                .or((qBoard.content.likeIgnoreCase("%" + keyword + "%")))));

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
