package com.aliens.backend.board.domain.repository.custom;

import com.aliens.backend.auth.domain.QMember;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MarketBoardResponse;
import com.aliens.backend.board.domain.*;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
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
        List<Tuple> results = queryFactory
                .select(qBoard, qMember, qMember.memberImage)
                .from(qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)

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
    public List<BoardResponse> getBoardPageWithCategory(final BoardCategory category, Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(qBoard, qMember, qMember.memberImage)
                .from(qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)

                .where(qBoard.category.eq(category))
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
    public List<BoardResponse> searchBoardPageWithKeyword(final String keyword, final Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(qBoard, qMember, qMember.memberImage)
                .from(qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)
                .where(qBoard.title.likeIgnoreCase("%" + keyword + "%")
                        .or(qBoard.content.likeIgnoreCase("%" + keyword + "%")))
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
    public List<MarketBoardResponse> getMarketBoardPage(final Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(qBoard, qBoard.marketInfo, qMember, qMember.memberImage)
                .from(qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)
                .where(qBoard.category.eq(BoardCategory.MARKET))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().distinct().toList();


        return results.stream()
                .map(tuple -> {
                    Board board = tuple.get(qBoard);
                    return board.getMarketBoardResponse();
                })
                .toList();
    }

    @Override
    public List<MarketBoardResponse> searchMarketBoardPage(final String keyword, final Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(qBoard, qBoard.marketInfo, qMember, qMember.memberImage)
                .from(qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)
                .where(qBoard.category.eq(BoardCategory.MARKET)
                        .and(qBoard.title.likeIgnoreCase("%" + keyword + "%")
                                .or((qBoard.content.likeIgnoreCase("%" + keyword + "%")))))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().distinct().toList();


        return results.stream()
                .map(tuple -> {
                    Board board = tuple.get(qBoard);
                    return board.getMarketBoardResponse();
                })
                .toList();
    }

    @Override
    public List<BoardResponse> getMyBoardPage(Long memberId, Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(qBoard, qBoard.marketInfo, qMember, qMember.memberImage)
                .from(qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)
                .where(qBoard.member.id.eq(memberId))

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

    public List<BoardResponse> searchBoardPageWithKeywordAndCategory(String keyword,
                                                                     BoardCategory category,
                                                                     Pageable pageable) {
        List<Tuple> results = queryFactory
                .select(qBoard, qBoard.marketInfo, qMember, qMember.memberImage)
                .from(qBoard)
                .leftJoin(qBoard.boardImages).fetchJoin()
                .leftJoin(qBoard.member, qMember).fetchJoin()
                .leftJoin(qMember.memberImage)
                .where(qBoard.category.eq(category)
                        .and(qBoard.title.likeIgnoreCase("%" + keyword + "%")
                                .or((qBoard.content.likeIgnoreCase("%" + keyword + "%")))))

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
