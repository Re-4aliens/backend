package com.aliens.backend.board.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.request.MarketChangeRequest;
import com.aliens.backend.board.controller.dto.request.MarketBoardCreateRequest;
import com.aliens.backend.board.service.BoardReadService;
import com.aliens.backend.board.service.BoardService;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.MarketBoardSuccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/boards/market")
@RestController
public class MarketController {

    private final BoardService boardService;
    private final BoardReadService boardReadService;

    public MarketController(final BoardService boardService, final BoardReadService boardReadService) {
        this.boardService = boardService;
        this.boardReadService = boardReadService;
    }

    @PostMapping
    public SuccessResponse<?> createMarketBoard(@Login final LoginMember loginMember,
                                  @RequestBody final MarketBoardCreateRequest request,
                                  @RequestPart final List<MultipartFile> marketBoardImages) {
        boardService.postMarketBoard(request, marketBoardImages, loginMember);

        return SuccessResponse.of(MarketBoardSuccess.CREATE_MARKET_BOARD_SUCCESS);
    }

    @GetMapping
    public SuccessResponse<?> getMarketBoardPage(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(MarketBoardSuccess.GET_MARKET_BOARD_PAGE_SUCCESS,
                boardReadService.getMarketBoardPage(pageable));
    }

    @GetMapping("/details")
    public SuccessResponse<?> getMarketBoardDetails(@RequestParam("id") final Long boardId) {
        return SuccessResponse.of(MarketBoardSuccess.GET_MARKET_BOARD_DETAILS_SUCCESS,
                boardReadService.getMarketBoardDetails(boardId));
    }

    @GetMapping("/search")
    public SuccessResponse<?> searchMarketBoards(@RequestParam("search-keyword") final String searchKeyword,
                                                     @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(MarketBoardSuccess.SEARCH_MARKET_BOARD_SUCCESS,
                boardReadService.searchMarketBoardPage(searchKeyword, pageable));
    }

    @PutMapping
    public SuccessResponse<?> changeMarketBoard(@Login final LoginMember loginMember,
                                             @RequestParam("id") final Long boardId,
                                             @RequestBody final MarketChangeRequest request) {
        boardService.changeMarketBoard(boardId, request, loginMember);

        return SuccessResponse.of(MarketBoardSuccess.CHANGE_MARKET_BOARD_SUCCESS);
    }
}
