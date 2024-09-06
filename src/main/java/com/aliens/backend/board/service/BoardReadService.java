package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MarketBoardResponse;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.board.domain.repository.BoardRepository;
import com.aliens.backend.board.domain.repository.custom.BoardCustomRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.BoardError;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class BoardReadService {

    private final BoardCustomRepository boardCustomRepository;
    private final BoardRepository boardRepository;

    public BoardReadService(final BoardCustomRepository boardCustomRepository,
                            final BoardRepository boardRepository) {
        this.boardCustomRepository = boardCustomRepository;
        this.boardRepository = boardRepository;
    }

    public List<BoardResponse> getBoardPage(final Pageable pageable) {
        return boardCustomRepository.getBoardPage(pageable);
    }

    public List<BoardResponse> getBoardPageWithCategory(String category, final Pageable pageable) {
        BoardCategory boardCategory = BoardCategory.from(category);
        return boardCustomRepository.getBoardPageWithCategory(boardCategory, pageable);
    }

    public List<BoardResponse> searchBoardPageWithKeyword(final String searchKeyword, final Pageable pageable) {
        return boardCustomRepository.searchBoardPageWithKeyword(searchKeyword, pageable);
    }

    public List<BoardResponse> getAnnouncePage(final Pageable pageable) {
        BoardCategory boardCategory = BoardCategory.ANNOUNCEMENT;
        return boardCustomRepository.getBoardPageWithCategory(boardCategory, pageable);
    }

    public List<MarketBoardResponse> getMarketBoardPage(final Pageable pageable) {
        return boardCustomRepository.getMarketBoardPage(pageable);
    }

    public MarketBoardResponse getMarketBoardDetails(final Long id) {
        Board board = getBoard(id);
        return board.getMarketBoardResponse();
    }

    private Board getBoard(final Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new RestApiException(BoardError.INVALID_BOARD_ID));
    }

    public List<MarketBoardResponse> searchMarketBoardPage(final String searchKeyword, final Pageable pageable) {
        return boardCustomRepository.searchMarketBoardPage(searchKeyword, pageable);
    }

    public List<BoardResponse> getMyBoardPage(final LoginMember loginMember, final Pageable pageable) {
        return boardCustomRepository.getMyBoardPage(loginMember.memberId(), pageable);
    }

    public List<BoardResponse> searchBoardPageWithKeywordAndCategory(final String searchKeyword, final String category, final Pageable pageable) {
        BoardCategory boardCategory = BoardCategory.from(category);
        return boardCustomRepository.searchBoardPageWithKeywordAndCategory(searchKeyword, boardCategory, pageable);
    }

    public BoardResponse getSingleBoard(Long id) {
        Board board = getBoard(id);
        return board.getBoardResponse();
    }
}
