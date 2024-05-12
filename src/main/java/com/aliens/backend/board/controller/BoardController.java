package com.aliens.backend.board.controller;


import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.board.controller.dto.request.ReportBoardRequest;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.service.BoardReadService;
import com.aliens.backend.board.service.BoardReportService;
import com.aliens.backend.board.service.BoardService;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.BoardSuccess;
import com.aliens.backend.global.response.success.ReportSuccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/boards")
@RestController
public class BoardController {

    private final BoardService boardService;
    private final BoardReadService boardReadService;
    private final BoardReportService boardReportService;

    public BoardController(final BoardService boardService, final BoardReadService boardReadService, final BoardReportService boardReportService) {
        this.boardService = boardService;
        this.boardReadService = boardReadService;
        this.boardReportService = boardReportService;
    }

    @PostMapping("/normal")
    public SuccessResponse<?> createBoard(@Login final LoginMember loginMember,
                                       @RequestPart final BoardCreateRequest request,
                                       @RequestPart final List<MultipartFile> boardImages) {
        boardService.postNormalBoard(request, boardImages, loginMember);
        return SuccessResponse.of(BoardSuccess.POST_BOARD_SUCCESS);
    }

    @GetMapping
    public SuccessResponse<List<BoardResponse>> getAllBoards(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return SuccessResponse.of(BoardSuccess.GET_ALL_BOARDS_SUCCESS,
                boardReadService.getBoardPage(pageable));
    }

    @GetMapping("/category")
    public SuccessResponse<List<BoardResponse>> getAllBoardsWithCategory(@RequestParam final String category,
                                                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(BoardSuccess.GET_ALL_BOARDS_WITH_CATEGORY_SUCCESS,
                boardReadService.getBoardPageWithCategory(category,pageable));
    }

    @GetMapping("/search")
    public SuccessResponse<List<BoardResponse>> searchAllBoardPage(@RequestParam("search-keyword") final String searchKeyword,
                                                                   @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(BoardSuccess.SEARCH_ALL_BOARDS_SUCCESS,
                boardReadService.searchBoardPageWithKeyword(searchKeyword,pageable));
    }

    @GetMapping("/announcements")
    public SuccessResponse<List<BoardResponse>> getAllAnnouncementBoards(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(BoardSuccess.GET_ALL_ANNOUNCEMENT_BOARDS_SUCCESS,
                boardReadService.getAnnouncePage(pageable));
    }

    @GetMapping("/writes")
    public SuccessResponse<List<BoardResponse>> getPageMyBoards(@Login LoginMember loginMember,
                                                                @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(BoardSuccess.GET_MY_BOARD_PAGE_SUCCESS,
                boardReadService.getMyBoardPage(loginMember, pageable));
    }

    @GetMapping("/category/search")
    public SuccessResponse<List<BoardResponse>> searchBoardsWithCategory(@RequestParam("search-keyword") final String searchKeyword,
                                                                         @RequestParam("category") final String category,
                                                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(BoardSuccess.SEARCH_BOARD_WITH_CATEGORY_SUCCESS,
                boardReadService.searchBoardPageWithKeywordAndCategory(searchKeyword, category, pageable));
    }

    @DeleteMapping
    public SuccessResponse<?> deleteBoard(@Login final LoginMember loginMember,
                            @RequestParam("id") final Long id) {
        boardService.deleteBoard(loginMember, id);
        return SuccessResponse.of(BoardSuccess.DELETE_BOARD_SUCCESS);
    }

    @PostMapping("/report")
    public SuccessResponse<?> reportBoard(@Login final LoginMember loginMember,
                                          @RequestBody final ReportBoardRequest request) {
        boardReportService.report(loginMember, request);
        return SuccessResponse.of(ReportSuccess.REPORT_BOARD_SUCCESS);
    }
}
