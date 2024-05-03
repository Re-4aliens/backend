package com.aliens.backend.board.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.board.controller.dto.request.ReportBoardRequest;
import com.aliens.backend.board.service.BoardReportService;
import com.aliens.backend.chat.service.ChatReportService;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.ReportSuccess;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/reports")
@RestController
public class ReportController {

    private final BoardReportService boardReportService;

    public ReportController(final ChatReportService chatReportService,
                            final BoardReportService boardReportService) {
        this.boardReportService = boardReportService;
    }

    @PostMapping("/board")
    public SuccessResponse<?> reportBoard(@Login final LoginMember loginMember,
                                          @RequestBody final ReportBoardRequest request) {
        boardReportService.report(loginMember, request);
        return SuccessResponse.of(ReportSuccess.REPORT_BOARD_SUCCESS);
    }
}
