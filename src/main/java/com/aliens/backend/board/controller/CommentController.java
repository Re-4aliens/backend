package com.aliens.backend.board.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.response.CommentResponse;
import com.aliens.backend.board.controller.dto.request.ParentCommentCreateRequest;
import com.aliens.backend.board.controller.dto.request.ChildCommentCreateRequest;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.service.CommentService;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.CommentSuccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments/parent")
    public SuccessResponse<?> createParentComment(@RequestBody final ParentCommentCreateRequest request,
                                                  @Login final LoginMember loginMember) {
        commentService.postParentComment(request, loginMember);

        return SuccessResponse.of(CommentSuccess.PARENT_COMMENT_CREATE_SUCCESS);
    }

    @PostMapping("/comments/child")
    public SuccessResponse<?> createChildComment(@RequestBody final ChildCommentCreateRequest request,
                                                 @Login final LoginMember loginMember) {
        commentService.postChildComment(request, loginMember);

        return SuccessResponse.of(CommentSuccess.CHILD_COMMENT_CREATE_SUCCESS);
    }

    @GetMapping("/comments/my-boards")
    public SuccessResponse<List<BoardResponse>> getPageMyCommentedBoards(@Login final LoginMember loginMember,
                                                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable) {
        return SuccessResponse.of(CommentSuccess.GET_MY_COMMENTED_BOARD_PAGE_SUCCESS,
                commentService.getCommentedBoardPage(loginMember, pageable));
    }

    @GetMapping("/comments/boards")
    public SuccessResponse<List<CommentResponse>> getCommentsByBoardId(@RequestParam("board-id") final Long boardId) {
        return SuccessResponse.of(CommentSuccess.GET_COMMENTS_BY_BOARD_ID_SUCCESS,
                commentService.getCommentsByBoardId(boardId));
    }

    @DeleteMapping("/comments")
    public SuccessResponse<?> deleteComment(@Login final LoginMember loginMember,
                                            @RequestParam("comment-id") final Long commentId) {
        commentService.deleteComment(loginMember, commentId);

        return SuccessResponse.of(CommentSuccess.DELETE_COMMENT_SUCCESS);
    }
}
