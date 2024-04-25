package com.aliens.backend.board.controller.dto.response;

import com.aliens.backend.board.domain.enums.CommentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {
    private final CommentStatus status;
    private final Long id;
    private final String content;
    private final Instant createdAt;
    private final MemberProfileDto memberProfileDto;
    private List<CommentResponse> children;

    public CommentResponse(CommentStatus status,
                           Long id,
                           String content,
                           Instant createdAt,
                           MemberProfileDto memberProfileDto) {
        this.status = status;
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.memberProfileDto = memberProfileDto;
    }

    public CommentResponse(CommentStatus status,
                           Long id,
                           String content,
                           Instant createdAt,
                           MemberProfileDto memberProfileDto,
                           List<CommentResponse> children) {
        this.status = status;
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.memberProfileDto = memberProfileDto;
        this.children = children;
    }

    public void setChildren(final List<CommentResponse> children) {
        this.children = children;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public MemberProfileDto getMemberProfileDto() {
        return memberProfileDto;
    }

    public List<CommentResponse> getChildren() {
        return children;
    }
}

