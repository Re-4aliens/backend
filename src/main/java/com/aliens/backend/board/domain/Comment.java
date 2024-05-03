package com.aliens.backend.board.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.controller.dto.response.CommentResponse;
import com.aliens.backend.board.controller.dto.request.ChildCommentCreateRequest;
import com.aliens.backend.board.domain.enums.CommentStatus;
import com.aliens.backend.board.domain.enums.CommentType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    @Column
    private CommentType type = CommentType.PARENT;

    @Enumerated(EnumType.STRING)
    @Column
    private CommentStatus status = CommentStatus.ACTIVE;

    @Column
    private Long parentCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Instant createdAt;

    protected Comment() {
    }

    private Comment(
            final String content,
            final Board board,
            final Member member
    ) {
        this.content = content;
        this.board = board;
        this.member = member;
    }

    public static Comment parentOf(
            final String content,
            final Board board,
            final Member member
    ) {
        return new Comment(
                content,
                board,
                member
        );
    }

    private Comment(
            final String content,
            final CommentType type,
            final Board board,
            final Long parentCommentId,
            final Member member
    ) {
        this.content = content;
        this.type = type;
        this.board = board;
        this.parentCommentId = parentCommentId;
        this.member = member;
    }

    public static Comment childOf(
            final ChildCommentCreateRequest request,
            final Board board,
            final Member member
    ) {
        return new Comment(
                request.content(),
                CommentType.CHILD,
                board,
                request.parentCommentId(),
                member
        );
    }

    public void deleteComment() {
        this.status = CommentStatus.DELETE;
    }

    public String getContent() {
        return content;
    }

    public Long getId() {
        return id;
    }

    public boolean isParent() {
        return type == CommentType.PARENT;
    }

    public CommentResponse getCommentResponse() {
        return new CommentResponse(
                status,
                id,
                content,
                createdAt,
                member.getprofileDto());
    }

    public boolean isChildFrom(final Long id) {
        return parentCommentId != null && parentCommentId.equals(id);
    }

    public boolean isWriter(final Long memberId) {
        return member.isSameId(memberId);
    }

    public CommentStatus getStatus() {
        return status;
    }
}
