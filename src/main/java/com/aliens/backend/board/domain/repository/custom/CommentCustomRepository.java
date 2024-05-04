package com.aliens.backend.board.domain.repository.custom;

import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.domain.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentCustomRepository {
    List<BoardResponse> getCommentedBoardPage(final Long memberId, final Pageable pageable);

    List<Comment> getCommentsByBoardId(final Long boardId);
}
