package com.aliens.backend.board.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.service.GreatService;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.GreatSuccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class GreatController {

    private final GreatService greatService;

    public GreatController(final GreatService greatService) {
        this.greatService = greatService;
    }

    @PostMapping("/great")
    public SuccessResponse<?> greatAtBoard(@RequestParam("board-id") final Long boardId,
                                           @Login final LoginMember loginMember) {
        greatService.greatAtBoard(boardId, loginMember);

        return SuccessResponse.of(GreatSuccess.GREAT_AT_BOARD_SUCCESS);
    }

    @GetMapping("/great/my-board")
    public SuccessResponse<List<BoardResponse>> getAllGreatBoards(@Login final LoginMember loginMember,
                                                                  @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(GreatSuccess.GET_ALL_GREAT_BOARDS_SUCCESS,
                greatService.getGreatBoardPage(loginMember, pageable));
    }
}
