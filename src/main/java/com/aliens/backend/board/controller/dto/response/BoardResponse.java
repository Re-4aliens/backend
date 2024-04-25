package com.aliens.backend.board.controller.dto.response;

import com.aliens.backend.board.domain.enums.BoardCategory;

import java.time.Instant;
import java.util.List;

public record BoardResponse(Long id,
        BoardCategory category,
        String title,
        String content,
        Long greatCount,
        Long commentCount,
        List<String> imageUrls,
        Instant createdAt,
        MemberProfileDto memberProfileDto) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nid").append(id);
        sb.append("\ncategory").append(category);
        sb.append("\ntitle").append(title);
        sb.append("\ncontent").append(content);
        sb.append("\ngreatCount").append(greatCount);
        sb.append("\ncommentCount").append(commentCount);
        sb.append("\nimageUrls").append(imageUrls);
        sb.append("\ncreatedAt").append(createdAt);
        return sb.toString();
    }
}
