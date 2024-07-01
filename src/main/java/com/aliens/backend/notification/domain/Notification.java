package com.aliens.backend.notification.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.notification.controller.dto.NotificationRequest;
import com.aliens.backend.notification.controller.dto.NotificationResponse;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "notificationId")
    private Long id;

    @Column
    private String content;

    @Column
    private boolean isRead = false;

    @Column
    private Long boardId;

    @Column
    private BoardCategory boardCategory;

    @Column(updatable = false)
    @CreatedDate
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    protected Notification(final BoardCategory boardCategory,
                           final String content,
                           final Long  boardId,
                           final Member member) {
        this.boardCategory = boardCategory;
        this.content = content;
        this.member = member;
        this.boardId = boardId;
    }

    protected Notification() {

    }

    public static Notification of(final NotificationRequest request, Member member) {
        return new Notification(request.boardCategory(),
                request.content(),
                request.boardId(),
                member);
    }

    public boolean isOwner(final Long memberId) {
        return member.getId() == memberId;
    }

    public void read() {
        isRead = true;
    }

    public NotificationResponse getNotificationResponse() {
        return new NotificationResponse(
                id,
                content,
                boardId,
                NotificationType.PERSONAL,
                boardCategory,
                createdAt,
                isRead
        );
    }

    public Long getId() {
        return id;
    }
}
