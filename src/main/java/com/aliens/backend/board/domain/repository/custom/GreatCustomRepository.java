package com.aliens.backend.board.domain.repository.custom;

import com.aliens.backend.board.controller.dto.response.BoardResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GreatCustomRepository {
    List<BoardResponse> getGreatBoardPage(final Long memberId, final Pageable pageable);
}
