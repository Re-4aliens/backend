package com.aliens.backend.notification.controller.dto;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.domain.enums.BoardCategory;

import java.util.List;

public record NotificationRequest(BoardCategory boardCategory,
                                  Long boardId,
                                  String content,
                                  List<Member> members) {
}
