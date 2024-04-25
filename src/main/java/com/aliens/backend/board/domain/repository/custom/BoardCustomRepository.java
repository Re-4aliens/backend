package com.aliens.backend.board.domain.repository.custom;

import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MarketBoardResponse;
import com.aliens.backend.board.domain.enums.BoardCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BoardCustomRepository {
    List<BoardResponse> getBoardPage(Pageable pageable);
    List<BoardResponse> getMyBoardPage(Long memberId, Pageable pageable);
    List<BoardResponse> getBoardPageWithCategory(BoardCategory boardCategory, Pageable pageable);
    List<MarketBoardResponse> getMarketBoardPage(Pageable pageable);

    List<BoardResponse> searchBoardPageWithKeyword(String keyword, Pageable pageable);
    List<MarketBoardResponse> searchMarketBoardPage(String keyword, Pageable pageable);
    List<BoardResponse> searchBoardPageWithKeywordAndCategory(String searchKeyword, BoardCategory boardCategory, Pageable pageable);
}
